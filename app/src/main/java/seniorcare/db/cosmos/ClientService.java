package seniorcare.db.cosmos;

import com.azure.cosmos.ConnectionPolicy;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClient;
import com.azure.cosmos.CosmosClientBuilder;
import com.google.android.gms.common.api.Api;
import com.google.common.collect.Lists;

public class ClientService {
    private final CosmosClient client;

    public ClientService() {
        System.out.println("Using Azure Cosmos DB endpoint: " + AccountSettings.HOST);

        ConnectionPolicy defaultPolicy = ConnectionPolicy.getDefaultPolicy();
        defaultPolicy.setUserAgentSuffix("CosmosDBJavaQuickstart");
        //  Setting the preferred location to Cosmos DB Account region
        //  West US is just an example. User should set preferred location to the Cosmos DB region closest to the application
        defaultPolicy.setPreferredLocations(Lists.newArrayList("West US"));

        this.client =  new CosmosClientBuilder()
                .setEndpoint(AccountSettings.HOST)
                .setKey(AccountSettings.MASTER_KEY)
                .setConnectionPolicy(defaultPolicy)
                .setConsistencyLevel(ConsistencyLevel.EVENTUAL)
                .buildClient();
    }
    public CosmosClient getClient() {
        return client;
    }

    public void closeClient() {
        client.close();
    }
}
