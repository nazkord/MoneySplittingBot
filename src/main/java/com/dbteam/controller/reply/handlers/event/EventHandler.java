package com.dbteam.controller.reply.handlers.event;

import com.dbteam.exception.NoCallbackQueryInUpdateException;
import com.dbteam.model.Event;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface EventHandler {
    SendMessage handleEvent(Update update);

    Event eventToHandle();

    default Long getChatId(Update update) {
        return update.getCallbackQuery().getMessage().getChatId();
    }

    default String getData(Update update) throws NoCallbackQueryInUpdateException {
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        else throw new NoCallbackQueryInUpdateException("Update must have a callback query!");
    }
}
