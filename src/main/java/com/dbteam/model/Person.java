package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Person {

    @Id
    String username;
    String fullName;
    Long groupChatId;
    Long chatId;
    String groupChatState;
    String botChatState;

    public void saveTo(Person person) {
        person.setUsername(this.username);
        person.setFullName(this.fullName);
        person.setGroupChatId(this.groupChatId);
        person.setChatId(this.chatId);
        person.setGroupChatId(this.groupChatId);
        person.setBotChatState(this.botChatState);
    }
} 