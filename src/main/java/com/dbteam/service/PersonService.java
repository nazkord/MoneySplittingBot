package com.dbteam.service;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Person;

public interface PersonService {
    void addPerson(Person person);

    void updatePerson(Person person);

    Person findPersonByUsername(String username) throws PersonNotFoundException;

    void updatePersonBotChatState(String username, String newState) throws PersonNotFoundException;

    void updatePersonGroupChatState(String username, String newState, Long groupChatId);

    String getPersonBotChatState(String username) throws PersonNotFoundException;

    String getPersonGroupChatState(String username, Long groupChatId) throws PersonNotFoundException;

}
