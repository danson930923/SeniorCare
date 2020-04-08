package com.example.seniorcare.db.dynamo;

import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
//import com.amazonaws.mobile.client.AWSMobileClient;
//import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchGetItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.TableKeysAndAttributes;
import com.example.seniorcare.db.context.IDbTableModelConvertible;

public class DynamoDbContext<T extends IDbTableModelConvertible> {
    private final DynamoDB dynamoDB;
    private Class classI;

    public DynamoDbContext(Context context, Class<T> classI) {
        this.classI = classI;

        // AWSMobileClient enables AWS user credentials to access your table
//        AWSMobileClient.getInstance().initialize(context).execute();

//        AWSCredentialsProvider credentialsProvider = AWSMobileClient.getInstance().getCredentialsProvider();
//        AWSConfiguration configuration = AWSMobileClient.getInstance().getConfiguration();


        // Add code to instantiate a AmazonDynamoDBClient
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
//                .withCredentials(new AWSCredentialsProvider() {
//                    @Override
//                    public AWSCredentials getCredentials() {
//                        return new AWSCredentials() {
//                            @Override
//                            public String getAWSAccessKeyId() {
//                                return "AKIAR7CTHLECGHEDBXPG";
//                            }
//
//                            @Override
//                            public String getAWSSecretKey() {
//                                return "f73udRWjRr/R0GQP9zHDNSYdDtfeBXR/ggvi/kTe";
//                            }
//                        };
//                    }
//
//                    @Override
//                    public void refresh() {
//
//                    }
//                })
                .withRegion(Regions.US_WEST_2)
                .build();

        this.dynamoDB = new DynamoDB(client);
    }

    public int list() {
        BatchGetItemOutcome result = dynamoDB.batchGetItem(new TableKeysAndAttributes(getNewInstance().getTableName()));
        return result.getTableItems().values().size();
    }
//
//    public void queryNews() {
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                NewsDO news = new NewsDO();
//                news.setUserId(unique-user-id);
//                news.setArticleId("Article1");
//
//                Condition rangeKeyCondition = new Condition()
//                        .withComparisonOperator(ComparisonOperator.BEGINS_WITH)
//                        .withAttributeValueList(new AttributeValue().withS("Trial"));
//
//                DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//                        .withHashKeyValues(note)
//                        .withRangeKeyCondition("articleId", rangeKeyCondition)
//                        .withConsistentRead(false);
//
//                PaginatedList<NewsDO> result = dynamoDBMapper.query(NewsDO.class, queryExpression);
//
//                Gson gson = new Gson();
//                StringBuilder stringBuilder = new StringBuilder();
//
//                // Loop through query results
//                for (int i = 0; i < result.size(); i++) {
//                    String jsonFormOfItem = gson.toJson(result.get(i));
//                    stringBuilder.append(jsonFormOfItem + "\n\n");
//                }
//
//                // Add your code here to deal with the data result
//                Log.d("Query result: ", stringBuilder.toString());
//
//                if (result.isEmpty()) {
//                    // There were no items matching your query.
//                }
//            }
//        }).start();
//    }

    private T getNewInstance() {
        try {
            return (T) classI.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
