package com.dbteam;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args) throws TelegramApiRequestException {

        configureLog4j();

        ApiContextInitializer.init();

        MoneySplittingBot mBot = new MoneySplittingBot();

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(mBot);
        } catch (TelegramApiException e) {
            throw new TelegramApiRequestException("Error while registering the bot the telegram API");
        }
    }

    private static void configureLog4j() {
        org.apache.log4j.BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);
    }
}