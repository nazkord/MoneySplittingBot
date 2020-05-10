package com.dbteam.reply.handlers.command;

import com.dbteam.model.Command;
import com.dbteam.model.Group;
import com.dbteam.service.GroupService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Collections;
import java.util.List;

@Service
public class BotAddedToGroupHandler implements CommandHandler {

    private static final String MESSAGE_GREETING =
            "Hello, thanks for adding me to your group.\n" +
                    "I won't let you down!\n\n" +
                    "Please, click the button below to join\n" +
                    "group expenses management.";

    private final GroupService groupService;

    public BotAddedToGroupHandler(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public SendMessage handleCommand(Update update) {

        groupService.addGroup(new Group(update.getMessage().getChatId(), Collections.emptyList()));

        InlineKeyboardButton button = new InlineKeyboardButton();
        button
                .setText("I am in!")
                .setCallbackData(Command.CALLBACK_DATA_BOT_ADDED_TO_GROUP.getValue());

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(button)));

        return new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(MESSAGE_GREETING)
                .setReplyMarkup(markup);
    }

    @Override
    public Command commandToHandle() {
        return Command.BOT_ADDED_TO_GROUP;
    }
}
