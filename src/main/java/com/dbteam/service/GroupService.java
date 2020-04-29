package com.dbteam.service;

import com.dbteam.exception.IllegalGroupChatIdException;
import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;

public interface GroupService {
    void addGroup(Group group);
    void updateGroup(Group group);
    Group findGroupById(Long groupChatId) throws IllegalGroupChatIdException;
    void addUserToGroup(Long groupChatId, Person person) throws IllegalGroupChatIdException;
    Boolean isUserInGroup(Long groupChatId, String username) throws IllegalGroupChatIdException, IllegalUsernameException;
}
