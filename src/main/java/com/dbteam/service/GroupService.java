package com.dbteam.service;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;

public interface GroupService {
    void addGroup(Group group);
    void updateGroup(Group group);
    Group findGroupById(Long groupChatId) throws GroupNotFoundException;
    void addUserToGroup(Long groupChatId, Person person) throws PersonNotFoundException, GroupNotFoundException;
    Boolean isUserInGroup(Long groupChatId, String username);
}
