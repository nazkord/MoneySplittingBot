package com.dbteam.ability;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Person;
import com.dbteam.model.ReplyHolder;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
public class StartHandler implements CommandHandler{

    private static final String MSG_START =
            "Hello! I am MoneySplittingBot. I can remember all your group expenses!";

    private final GroupService groupService;
    private final PersonService personService;

    public StartHandler(GroupService groupService, PersonService personService) {
        this.groupService = groupService;
        this.personService = personService;
    }

    @Override
    public SendMessage primaryAction(Update update) {
        Person person;

        try {
            person = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            person = getPersonFrom(update);
            personService.addPerson(person);
        }

        // do something based on whether user has any group

        SendMessage message = new SendMessage();
        message.setText(MSG_START);
        message.setChatId(update.getMessage().getChatId());
        return message;
    }

    private String getUsername(Update update) {
        return update.getMessage().getFrom().getUserName();
    }

    private Person getPersonFrom(Update update) {
        User user = update.getCallbackQuery().getFrom();
        return new Person(user.getUserName(),
                user.getFirstName() + " " + user.getLastName(),
                null, update.getMessage().getChatId(),
                null, null);
    }

    @Override
    public List<ReplyHolder> getReplyHolders(Update update) {
        return null;
    }

    @Override
    public Command commandToHandle() {
        return Command.START;
    }
}
