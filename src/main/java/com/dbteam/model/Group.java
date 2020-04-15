package com.dbteam.model;

import lombok.Data;

import java.util.List;

@Data
public class Group {
    Long groupId;
    Long groupChatId;
    List<User> users;
}
