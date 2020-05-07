package com.dbteam.service.serviceImpl;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.IllegalGroupChatIdException;
import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;
import com.dbteam.repository.GroupRepository;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import com.google.inject.internal.cglib.core.$ObjectSwitchCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private PersonService personService;

    @Override
    public void addGroup(Group group) {
        groupRepository.save(group);
    }

    @Override
    public void updateGroup(Group group) {
        Group newGroup = new Group();
        group.saveTo(newGroup);
        groupRepository.save(newGroup);
    }

    @Override
    public Group findGroupById(Long groupChatId) throws GroupNotFoundException {
        Optional<Group> optionalGroup = groupRepository.findGroupByGroupChatId(groupChatId);
        return optionalGroup.orElseThrow(() -> new GroupNotFoundException("There is no group in db with chat id: " + groupChatId));
    }

    @Override
    public void addUserToGroup(Long groupChatId, Person person) throws PersonNotFoundException, GroupNotFoundException {
        Group group = findGroupById(groupChatId);
        try {
            personService.findPersonByUsername(person.getUsername());
        } catch (PersonNotFoundException e) {
            personService.addPerson(person);
        } finally {
            group.getPeople().add(person);
            updateGroup(group);
        }
    }

    @Override
    public Boolean isUserInGroup(Long groupChatId, String username) throws GroupNotFoundException, PersonNotFoundException {
        Group group = findGroupById(groupChatId);
        Person person = personService.findPersonByUsername(username);
        return group.getPeople().contains(person);
    }
}

