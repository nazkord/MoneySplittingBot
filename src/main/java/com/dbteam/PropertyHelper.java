package com.dbteam;

import java.io.IOException;

public class PropertyHelper {
    private static final String BOT_TOKEN_FILENAME = "telegramBotToken.txt";
    private static final String BOT_USERNAME_FILENAME = "telegramBotUsername.txt";
    private static final String MONGO_USERNAME_FILENAME = "mongodbUsername.txt";
    private static final String MONGO_PASSWORD_FILENAME = "mongodbPassword.txt";

    public static String getBotToken() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + BOT_TOKEN_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }

    public static String getBotUsername() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + BOT_USERNAME_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }

    public static String getMongoUsername() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + MONGO_USERNAME_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }

    public static String getMongoPassword() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + MONGO_PASSWORD_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }

}
