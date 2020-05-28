package com.dbteam.repository;

import com.dbteam.model.db.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @BeforeAll
    public void setUp() {
        Map<Long, String> groupChatStates = new HashMap<>();
        groupChatStates.put(10L, "saying_hi");
        groupChatStates.put(11L, "saying_bye");


        Person N = new Person("okok", "Nazarczyk", 10L, groupChatStates, null);
        Person K = new Person("kubakhj", "Kubus", 10L, groupChatStates, null);
        Person J = new Person("jan", "Jasiek", 10L, groupChatStates, null);

        personRepository.saveAll(List.of(N, K, J));
    }

    @Test
    void findUserByUsername() {
        //given
        Person expectedPerson = new Person("Vasia", "Vasia Pupkin", 10L, Map.of(2L, ""), null);
        personRepository.save(expectedPerson);

        //when
        Optional<Person> actualPerson = personRepository.findUserByUsername("Vasia");

        //then
        assertTrue(actualPerson.isPresent());
        assertEquals(expectedPerson, actualPerson.get());
    }

    @Test
    void test() {
        // when
        Optional<Person> person = personRepository.findUserByUsername("okok");

        // then
        assertTrue(person.get().getGroupChatsStates().containsKey(10L));
        assertTrue(person.get().getGroupChatsStates().containsKey(11L));
        assertEquals("saying_hi", person.get().getGroupChatsStates().get(10L));
        assertEquals("saying_bye", person.get().getGroupChatsStates().get(11L));
    }
}