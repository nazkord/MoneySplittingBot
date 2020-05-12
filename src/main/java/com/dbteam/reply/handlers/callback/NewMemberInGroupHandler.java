package com.dbteam.reply.handlers.callback;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Callback;
import com.dbteam.model.Person;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;

@Service
public class NewMemberInGroupHandler implements CallbackHandler {

    private final GroupService groupService;
    private final PersonService personService;

    public NewMemberInGroupHandler(GroupService groupService, PersonService personService) {
        this.groupService = groupService;
        this.personService = personService;
    }

    @Override
    public SendMessage sendMessage(Update update) {

        Long chatId = getChatId(update);
        SendMessage ms = new SendMessage().setChatId(chatId);
        try {
            groupService.addUserToGroup(chatId, getPersonFrom(update));
            ms.setText("@" + update.getCallbackQuery().getFrom().getUserName() + " has been added");
        } catch (GroupNotFoundException e) {
            ms.setText("There is no such group!");
        } catch (PersonNotFoundException e) {
            ms.setText("There is no such user!");
        }

        return ms;
    }

    @Override
    public AnswerCallbackQuery handleCallback(Update update) {
        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer.setText("You're in!");
        answer.setCallbackQueryId(update.getCallbackQuery().getId());
        return answer;
    }

    @Override
    public Callback callbackToHandle() {
        return Callback.NEW_MEMBER_IN_GROUP;
    }

    private Person getPersonFrom(Update update) {
        User user = update.getCallbackQuery().getFrom();
        return new Person(user.getUserName(),
                user.getFirstName() + " " + user.getLastName(),
                getChatId(update), new HashMap<>(),
                null);
    }
}