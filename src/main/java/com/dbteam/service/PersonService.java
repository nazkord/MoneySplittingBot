package com.dbteam.service;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Person;

import java.util.List;

public interface PersonService {
    void addPerson(Person person);

    void updatePerson(Person person);

    Person findPersonByUsername(String username) throws PersonNotFoundException;

    void updatePersonBotChatState(String username, String newState) throws PersonNotFoundException;

    void updatePersonGroupChatState(String username, String newState) throws PersonNotFoundException;

}
