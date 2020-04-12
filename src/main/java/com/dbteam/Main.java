package com.dbteam;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {

    private static final String DATABASE_NAME = "botdb";

    public static void main(String[] args) throws TelegramApiRequestException {

        configureLog4j();

        ApiContextInitializer.init();

        MongoDatabase db = getMongoDatabase();
        db.getCollection("users").insertOne(new Document("username", "checkCheck")
                .append("groupId", 2));

        MoneySplittingBot mBot = new MoneySplittingBot();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(mBot);
        } catch (TelegramApiException e) {
            throw new TelegramApiRequestException("Error while registering the bot the telegram API");
        }
    }

    private static MongoDatabase getMongoDatabase() {
        String username = PropertyHelper.getMongoUsername();
        String password = PropertyHelper.getMongoPassword();
        MongoClient mongoClient = MongoClients.create(
                "mongodb+srv://" + username + ":" + password +
                        "@moneybot-qwdm2.mongodb.net/test?retryWrites=true&w=majority");
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    private static void configureLog4j() {
        org.apache.log4j.BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
    }
}