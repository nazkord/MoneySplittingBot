package com.dbteam.controller.ability;


import com.dbteam.model.Command;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Handles user commands like /start, /help and so on.
 */
public interface CommandHandler {

    List<BotApiMethod<?>> primaryAction(Update update);

    List<BotApiMethod<?>> secondaryAction(Update update);

    Command commandToHandle();

    default String getUsername(Update update) {
        if (update.hasCallbackQuery())
            return update.getCallbackQuery().getFrom().getUserName();
        return update.getMessage().getFrom().getUserName();
    }

    default String getDataPostfix(String data) {
        return data.split("/")[1];
    }
}
