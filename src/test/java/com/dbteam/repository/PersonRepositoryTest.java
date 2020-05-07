package com.dbteam.repository;

import com.dbteam.model.Person;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
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
        Person N = new Person("nazkord", "Nazarczyk", 10L, 2L, null, null);
        Person K = new Person("kubakhj", "Kubus", 10L, 3L, null, null);
        Person J = new Person("jan", "Jasiek", 10L, 4L, null, null);

        personRepository.saveAll(List.of(N, K, J));
    }

    @Test
    void findUserByUsername() {
        //given
        Person expectedPerson = new Person("Vasia", "Vasia Pupkin", 1L,2L, null, null);
        personRepository.save(expectedPerson);

        //when
        Optional<Person> actualPerson = personRepository.findUserByUsername("Vasia");

        //then
        assertTrue(actualPerson.isPresent());
        assertEquals(expectedPerson, actualPerson.get());
    }
}