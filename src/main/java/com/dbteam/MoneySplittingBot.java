package com.dbteam;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MoneySplittingBot extends TelegramLongPollingBot {

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
        return PropertyHelper.getBotUsername();
    }

    public String getBotToken() {
        return PropertyHelper.getBotToken();
    }
}
