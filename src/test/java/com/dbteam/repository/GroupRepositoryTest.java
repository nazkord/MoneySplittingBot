package com.dbteam.repository;

import com.dbteam.model.Group;
import com.dbteam.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GroupRepositoryTest {

    @Test
    public void findGroupByGroupChatId(@Autowired GroupRepository groupRepository) {
        //given
        Person person = new Person("Pronia", "Pronia", 1L, null, null);
        Group expectedGroup = new Group(1L,"Pronia's Group", Collections.singletonList(person));
        groupRepository.save(expectedGroup);

        //when
        Optional<Group> actualGroup = groupRepository.findGroupByGroupChatId(1L);

        //then
        assertTrue(actualGroup.isPresent());
        assertEquals(expectedGroup, actualGroup.get());
    }
}