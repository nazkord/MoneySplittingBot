package com.dbteam.reply.handlers.command;

import com.dbteam.model.Command;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandHandler {
    SendMessage handleCommand(Update update);

    Command commandToHandle();

    default Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    default String getData(Update update) {
        return update.getCallbackQuery().getData();
    }
}
