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

    SendMessage primaryAction(Update update);

    List<BotApiMethod<?>> secondaryAction(Update update);

    Command commandToHandle();
}
