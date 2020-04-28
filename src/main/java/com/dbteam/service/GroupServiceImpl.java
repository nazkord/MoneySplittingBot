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

    @Autowired
    private PersonService personService = new PersonServiceImpl();

    @Override
    public Group findGroupById(Long groupChatId) throws IllegalGroupChatIdException {
        Optional<Group> optionalGroup = groupRepository.findGroupByGroupChatId(groupChatId);
        return optionalGroup.orElseThrow(() -> new IllegalGroupChatIdException("There is no group in db with chat id: " + groupChatId));
    }

    @Override
    public void addUserToGroup(Long groupChatId, Person person) throws IllegalGroupChatIdException {
        Group group = findGroupById(groupChatId);
        personService.addPerson(person);
        group.getPeople().add(person);
    }

    @Override
    public Boolean isUserInGroup(Long groupChatId, String username) throws IllegalGroupChatIdException, IllegalUsernameException {
        Group group = findGroupById(groupChatId);
        Person person = personService.findPersonByUsername(username);
        return group.getPeople().contains(person);
    }
}

