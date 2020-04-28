package com.dbteam.service;

import com.dbteam.exception.IllegalGroupChatIdException;
import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;
import com.dbteam.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

public class GroupServiceImpl implements GroupService{

    @Autowired
    private GroupRepository groupRepository;

    private PersonService personService= new PersonServiceImpl();

    @Override
    public Group findGroupById(long groupChatId) throws IllegalGroupChatIdException {
        Optional<Group> optionalGroup = groupRepository.findGroupByGroupChatId(groupChatId);
        return optionalGroup.orElseThrow(() -> new IllegalGroupChatIdException("There is no group in db with chat id: " + groupChatId));
    }

    @Override
    public void addUserToGroup(long groupChatId, String username) throws IllegalGroupChatIdException {
        Group group = findGroupById(groupChatId);
        Person person;
        try {
            person = personService.findPersonByUsername(username);
        }
        catch (IllegalUsernameException e){
            person = new Person();
            person.setUsername(username);
            personService.addPerson(person);
        }
        group.addUser(person);
    }

    @Override
    public Boolean isUserInGroup(long groupChatId, String username) throws IllegalGroupChatIdException, IllegalUsernameException {
        Group group = findGroupById(groupChatId);
        Person person = personService.findPersonByUsername(username);
        return group.getPeople().contains(person);
    }
}

