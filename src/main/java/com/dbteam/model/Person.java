package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class Person {

    @Id
    String username;
    String fullName;
    Long chatId;
    Map<Long, String> groupChatsStates;
    String botChatState;

    public void saveTo(Person person) {
        person.setUsername(this.username);
        person.setFullName(this.fullName);
        person.setChatId(this.chatId);
        person.setBotChatState(this.botChatState);
        person.setGroupChatsStates(this.groupChatsStates);
    }
} 