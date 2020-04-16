package com.dbteam.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Group {

    @Id
    Long groupId;
    Long groupChatId;
    List<User> users;
}
