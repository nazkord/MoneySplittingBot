package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@Document(collection = "users")
public class Person {

    @Id
    String username;
    String fullName;
    Long groupId;
    Long chatId;
    String state;
}
