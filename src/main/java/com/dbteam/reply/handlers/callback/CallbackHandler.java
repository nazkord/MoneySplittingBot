package com.dbteam.reply.handlers.callback;

import com.dbteam.exception.NoCallbackQueryInUpdateException;
import com.dbteam.model.Callback;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallbackHandler {

    AnswerCallbackQuery handleCallback(Update update);

    Callback callbackToHandle();

    SendMessage sendMessage(Update update);

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
