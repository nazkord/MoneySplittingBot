package com.dbteam;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public class MoneySplittingBot extends TelegramLongPollingBot {
    
    public static final String BOT_TOKEN_FILENAME = "telegramBotToken.txt";
    public static final String BOT_USERNAME_FILENAME = "telegramBotUsername.txt";

    public void onUpdateReceived(Update update) {

        //here the magic happens ;)
        Message received = update.getMessage();
        SendMessage newMessage = new SendMessage(received.getChatId(), received.getText());

        try {
            execute(newMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public String getBotUsername() {
        return getBotUsernameFromProperties();
    }

    public String getBotToken() {
        return getBotTokenFromProperties();
    }

    private String getBotTokenFromProperties() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + BOT_TOKEN_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }

    private String getBotUsernameFromProperties() {
        try {
            return new String(MoneySplittingBot.class.getResourceAsStream("/" + BOT_USERNAME_FILENAME).readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Exception while reading from resources");
        }
    }
}
