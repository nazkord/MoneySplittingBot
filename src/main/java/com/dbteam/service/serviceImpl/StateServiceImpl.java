package com.dbteam.service.serviceImpl;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Person;
import com.dbteam.service.PersonService;
import com.dbteam.service.StateService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class StateServiceImpl implements StateService {

    private final PersonService personService;

    public StateServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean updateFitsBotChatState(Update update, String expectedState) {
        String username;
        if (update.hasMessage())
            username = update.getMessage().getFrom().getUserName();
        else if (update.hasCallbackQuery())
            username = update.getCallbackQuery().getMessage().getFrom().getUserName();
        else
            return false;

        Person person;

        try {
            person = personService.findPersonByUsername(username);
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return person.getBotChatState().equals(expectedState);

    }

    @Override
    public String buildBotChatState(String command, String groupChatId, String... optional) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(command)
                .append("/")
                .append(groupChatId);

        for (String o: optional) {
            builder
                    .append("/")
                    .append(o);
        }

        return builder.toString();
    }
}
