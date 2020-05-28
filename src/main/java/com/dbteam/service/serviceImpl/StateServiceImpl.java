package com.dbteam.service.serviceImpl;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.db.Person;
import com.dbteam.service.PersonService;
import com.dbteam.service.StateService;
import org.springframework.stereotype.Service;

@Service
public class StateServiceImpl implements StateService {

    private final PersonService personService;

    public StateServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean isUserBotChatStateStartsWith(String username, String expectedState) throws PersonNotFoundException {
        Person person = personService.findPersonByUsername(username);
        return person.getBotChatState().startsWith(expectedState);
    }

    @Override
    public String buildBotChatState(String command, String... optional) {
        StringBuilder builder = new StringBuilder();
        builder.append(command);

        for (String o: optional) {
            builder
                    .append("/")
                    .append(o);
        }

        return builder.toString();
    }

    @Override
    public String getUserBotChatState(String username) throws PersonNotFoundException {
        Person person = personService.findPersonByUsername(username);
        return person.getBotChatState();
    }

    @Override
    public String getUserGroupChatState(String username, Long groupChatId) throws PersonNotFoundException {
        Person person = personService.findPersonByUsername(username);
        return person.getGroupChatsStates().get(groupChatId);
    }
}
