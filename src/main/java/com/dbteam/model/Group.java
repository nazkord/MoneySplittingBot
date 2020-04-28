package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "groups")
public class Group {

    @Id
    Long groupChatId;
    List<Person> people;

    public void addUser(Person newUser){
        this.people.add(newUser);
    }

    public List<Person> getPeople(){
        return this.people;
    }
}
