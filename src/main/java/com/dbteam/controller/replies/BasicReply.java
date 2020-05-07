package com.dbteam.controller.replies;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class BasicReply {

    protected SilentSender silent;

    protected AbilityBot bot;


    public BasicReply(SilentSender silent, AbilityBot bot) {
        this.silent = silent;
        this.bot = bot;
    }

    protected void execute(SendMessage sm) {
        try {
            bot.execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public abstract Reply get();
}
