package com.dbteam.ability;


import com.dbteam.model.Command;
import com.dbteam.model.ReplyHolder;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * Handles user commands like /start, /help and so on.
 */
public interface CommandHandler {

    SendMessage primaryAction(Update update);

    List<ReplyHolder> getReplyHolders(Update update);

    Command commandToHandle();
}
