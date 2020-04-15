package com.dbteam.model;

import lombok.Data;

@Data
public class User {
    String username;
    Long groupId;
    Long chatId;
    String state;
}
