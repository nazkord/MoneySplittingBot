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
    public boolean userHasSuchBotChatState(String username, String expectedState) throws PersonNotFoundException {
        Person person = personService.findPersonByUsername(username);
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

    @Override
    public String getBotChatStateOfUser(String username) throws PersonNotFoundException {
        Person person = personService.findPersonByUsername(username);
        return person.getBotChatState();
    }
}
