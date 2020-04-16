package com.dbteam.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class User {

    @Id
    String username;
    Long groupId;
    Long chatId;
    String state;
}
