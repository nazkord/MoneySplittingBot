package com.dbteam.service;

import com.dbteam.exception.IllegalGroupChatIdException;
import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Group;

public interface GroupService {
    Group findGroupById(long groupChatId) throws IllegalGroupChatIdException;
    void addUserToGroup(long groupChatId, String username) throws IllegalGroupChatIdException;
    Boolean isUserInGroup(long groupChatId, String username) throws IllegalGroupChatIdException, IllegalUsernameException;
}
