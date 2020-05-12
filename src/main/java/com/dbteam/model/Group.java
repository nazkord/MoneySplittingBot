package com.dbteam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "groups")
public class Group {

    @Id
    Long groupChatId;
    String groupTitle;
    List<Person> people;

    public void saveTo(Group group) {
        group.setGroupChatId(this.groupChatId);
        group.setPeople(this.people);
        group.setGroupTitle(groupTitle);
    }

}
