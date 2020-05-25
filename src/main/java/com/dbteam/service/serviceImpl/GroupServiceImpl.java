package com.dbteam.service.serviceImpl;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;
import com.dbteam.repository.GroupRepository;
import com.dbteam.service.GroupService;
import com.dbteam.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final PersonService personService;

    public GroupServiceImpl(GroupRepository groupRepository, PersonService personService) {
        this.groupRepository = groupRepository;
        this.personService = personService;
    }

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
    public void addUserToGroup(Long groupChatId, Person person) throws GroupNotFoundException {
        Group group = findGroupById(groupChatId);
        try {
            person = personService.findPersonByUsername(person.getUsername());
        } catch (PersonNotFoundException e) {
            personService.addPerson(person);
        } finally {
            //TODO: add some initial state for the group
            //TODO: maybe it's better to move this line to PersonService
            if (person.getGroupChatsStates() == null) person.setGroupChatsStates(new HashMap<>());
            person.getGroupChatsStates().put(groupChatId, "INITIAL_STATE");
            personService.updatePerson(person);
            group.getPeople().add(person);
            //TODO: add groupChatId to list of groupIds in person
            updateGroup(group);
        }
    }

    @Override
    public Boolean isUserInGroup(Long groupChatId, String username) {
        Group group = null;
        try {
            group = findGroupById(groupChatId);
        } catch (GroupNotFoundException e) {
            return false;
        }
        return group.getPeople().stream().anyMatch(p -> p.getUsername().equals(username));
    }
}

