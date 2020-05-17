package com.dbteam.controller.ability;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Person;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StartHandler implements CommandHandler{

    private static final String MSG_START =
            "Hello! I am MoneySplittingBot. I can remember all your group expenses. ";
    private static final String MSG_NO_GROUPS =
            "Add me to your group chat and I will show you what I can!";

    private final GroupService groupService;
    private final PersonService personService;

    public StartHandler(GroupService groupService, PersonService personService) {
        this.groupService = groupService;
        this.personService = personService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        StringBuilder reply = new StringBuilder();
        reply.append(MSG_START);
        Person person;

        try {
            person = personService.findPersonByUsername(getUsername(update));
        } catch (PersonNotFoundException e) {
            person = getPersonFrom(update);
            personService.addPerson(person);
        }

        Map<Long, String> groupChatStates = person.getGroupChatsStates();

        if (groupChatStates == null || groupChatStates.isEmpty()) {
            reply.append(MSG_NO_GROUPS);
        } else {
            reply
                    .append("You have ")
                    .append(person.getGroupChatsStates().size())
                    .append(" groups. Type '/' to see available commands.");
        }

        SendMessage message = new SendMessage();
        message.setText(reply.toString());
        message.setChatId(update.getMessage().getChatId());

        person.setChatId(update.getMessage().getChatId());
        personService.addPerson(person);

        return List.of(message);
    }

    private String getUsername(Update update) {
        return update.getMessage().getFrom().getUserName();
    }

    private Person getPersonFrom(Update update) {
        User user = update.getCallbackQuery().getFrom();
        return new Person(user.getUserName(),
                user.getFirstName() + " " + user.getLastName(),
                update.getMessage().getChatId(), new HashMap<>(),
                null);
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {
        return null;
    }

    @Override
    public Command commandToHandle() {
        return Command.START;
    }
}
