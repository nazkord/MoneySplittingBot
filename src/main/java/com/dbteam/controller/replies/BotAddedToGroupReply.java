package com.dbteam.controller.replies;

import com.dbteam.model.Group;
import com.dbteam.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class BotAddedToGroupReply extends BasicReply{

    private static final String MESSAGE_GREETING =
            "Hello, thanks for adding me to your group.\n" +
                    "I won't let you down!\n\n" +
                    "Please, click the button below to join\n" +
                    "group expenses management.";

//    @Autowired
//    private GroupService groupService;


    public BotAddedToGroupReply(SilentSender silent, AbilityBot bot) {
        super(silent, bot);
    }

    public Reply get() {

        return Reply.of(action, conditions);

    }

//         groupService.addGroup(new Group(update.getMessage().getChatId(), List.of()));

    private final Consumer<Update> action = (update -> {

        InlineKeyboardButton button = new InlineKeyboardButton();
        button
                .setText("I am in!")
                .setCallbackData("new_member");


        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(List.of(List.of(button)));

        SendMessage sm = new SendMessage()
                .setChatId(update.getMessage().getChatId())
                .setText(MESSAGE_GREETING)
                .setReplyMarkup(markup);

        execute(sm); // wrapper for bot.execute()

    });

    private final List<Predicate<Update>> conditions = List.of(
            (update) -> {
                boolean flag = false;
                if (update.hasMessage() && update.getMessage().getNewChatMembers() != null) {
                    for (User u: update.getMessage().getNewChatMembers()) {
                        if (u.getUserName().equals("money_splitting_bot"))
                            flag = true;
                    }
                }
                return flag;
            }
    );
}
