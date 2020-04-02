package seniorcare.db.cosmos;

import com.azure.cosmos.CosmosContainer;
import com.azure.cosmos.CosmosContainerProperties;
import com.azure.cosmos.CosmosDatabase;
import com.azure.cosmos.CosmosItemProperties;
import com.azure.cosmos.CosmosItemRequestOptions;
import com.azure.cosmos.CosmosItemResponse;
import com.azure.cosmos.FeedOptions;
import com.azure.cosmos.FeedResponse;
import com.azure.cosmos.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import seniorcare.db.context.DataType;
import seniorcare.db.context.IDbTableModelConvertible;

public class DbContext<T extends IDbTableModelConvertible> {
    private final T sample;

//    private CosmosClient client;

//    private final String databaseName = "ToDoList";
//    private final String containerName = "Items";

    private CosmosDatabase database;
    private CosmosContainer container;
    private ClientService clientService;

    public DbContext(ClientService clientService, T sample) {
        this.clientService = clientService;
        this.sample = (T)sample.getNewInstance();


        createDatabaseIfNotExists();
        createContainerIfNotExists();
//        scaleContainer();
    }

    private void getClient() throws Exception {
        //  </CreateSyncClient>

    }

    private void createDatabaseIfNotExists(){
        try {
            String databaseName = AccountSettings.DATABASE_NAME;

            System.out.println("Create database " + databaseName + " if not exists.");

            //  Create database if not exists
            //  <CreateDatabaseIfNotExists>
            database = clientService.getClient().createDatabaseIfNotExists(databaseName).getDatabase();
            //  </CreateDatabaseIfNotExists>

            System.out.println("Checking database " + database.getId() + " completed!\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createContainerIfNotExists() {
        try {
            String containerName = sample.getTableName();

            System.out.println("Create container " + containerName + " if not exists.");

            //  Create container if not exists
            //  <CreateContainerIfNotExists>
            CosmosContainerProperties containerProperties =
                    new CosmosContainerProperties(containerName, sample.getPrimaryKey());

            //  Create container with 400 RU/s
            container = database.createContainerIfNotExists(containerProperties, 400).getContainer();
            //  </CreateContainerIfNotExists>

            System.out.println("Checking container " + container.getId() + " completed!\n");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void insertData(T newInstance) throws Exception {
        //  <CreateItem>
        //  Create item using container that we created using sync client

        //  Use lastName as partitionKey for cosmos item
        //  Using appropriate partition key improves the performance of database operations
        CosmosItemRequestOptions cosmosItemRequestOptions = new CosmosItemRequestOptions(newInstance.getPrimaryKeyValue());
        CosmosItemResponse item = container.createItem(newInstance, cosmosItemRequestOptions);
        //  </CreateItem>

        //  Get request charge and other properties like latency, and diagnostics strings, etc.
        System.out.println(String.format("Created item with request charge of %.2f within" +
                        " duration %s",
                item.getRequestCharge(), item.getRequestLatency()));
    }


    public List<T> queryItems(T criteria) {
        //  <QueryItems>
        // Set some common query options
        FeedOptions queryOptions = new FeedOptions();
        queryOptions.maxItemCount(100);
        queryOptions.setEnableCrossPartitionQuery(true);
        //  Set populate query metrics to get metrics around query executions
        queryOptions.populateQueryMetrics(true);

        StringBuilder queryBuilder = null;
        HashMap<String, Object> allData = criteria.getAllData();
        for (String key : allData.keySet()) {
            String value = allData.get(key).toString();
            if (value == null || value == "") {
                continue;
            }

            if (queryBuilder == null) {
                queryBuilder = new StringBuilder();
            } else  {
                queryBuilder.append(" AND ");
            }

            queryBuilder.append(key);
            queryBuilder.append(" = ");
            queryBuilder.append(value);
        }
        Iterator<FeedResponse<CosmosItemProperties>> feedResponseIterator = container.queryItems(
                queryBuilder.toString(), queryOptions);

        List<T> resultList = new ArrayList<>();
        feedResponseIterator.forEachRemaining(cosmosItemPropertiesFeedResponse -> {
            System.out.println("Got a page of query result with " +
                    cosmosItemPropertiesFeedResponse.getResults().size() + " items(s)"
                    + " and request charge of " + cosmosItemPropertiesFeedResponse.getRequestCharge());

            System.out.println("Item Ids " + cosmosItemPropertiesFeedResponse
                    .getResults()
                    .stream()
                    .map(Resource::getId)
                    .collect(Collectors.toList()));

            cosmosItemPropertiesFeedResponse
                    .getResults()
                    .stream()
                    .forEach(cosmosItemProperties -> resultList.add(getData(cosmosItemProperties)));
        });
        //  </QueryItems>

        return resultList;
    }

    private T getInstance() {
        return (T) sample.getNewInstance();
    }


    private T getData (CosmosItemProperties cosmosItemProperties) {
        T newInstance = getInstance();

        for (String columnName : newInstance.getTableColumnsKeyType().keySet()) {
            newInstance.setData(
                    columnName,
                    readCosmosItemProperties(
                            cosmosItemProperties,
                            columnName,
                            newInstance.getTableColumnsKeyType().get(columnName)
                    )
            );
        }

        return newInstance;
    }

    private Object readCosmosItemProperties (CosmosItemProperties cosmosItemProperties, String columnName, DataType dataType) {
        switch(dataType) {
            case INTEGER:
                return cosmosItemProperties.getInt(columnName);
            case TEXT:
                return cosmosItemProperties.getString(columnName);
        }
        return null;
    }
}
