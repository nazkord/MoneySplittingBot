package com.dbteam.dbManagement;

import com.dbteam.PropertyHelper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class DatabaseManager {

    private MongoClient mongoClientInstance;

    //private constructor to avoid client applications to use constructor
    private DatabaseManager() {
        initMongoClient();
    }

    private void initMongoClient() {
        String username = PropertyHelper.getMongoUsername();
        String password = PropertyHelper.getMongoPassword();
        mongoClientInstance = MongoClients.create(
                "mongodb+srv://" + username + ":" + password +
                        "@moneybot-qwdm2.mongodb.net/test?retryWrites=true&w=majority");
    }


}
