package com.dbteam.service;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Person;

import java.util.List;

public interface PersonService {
    void addPerson(Person person);

    void updatePerson(Person person);

    Person findPersonByUsername(String username) throws IllegalUsernameException;

    void updatePersonBotChatState(String username, String newState) throws IllegalUsernameException;

    void updatePersonGroupChatState(String username, String newState) throws IllegalUsernameException;

    List<Person> getPersonsOfGroup(Long groupChatId);
}
