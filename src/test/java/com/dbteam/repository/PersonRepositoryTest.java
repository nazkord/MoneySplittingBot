package com.dbteam.repository;

import com.dbteam.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
public class PersonRepositoryTest {

    @Test
    void findUserByUsername(@Autowired PersonRepository personRepository) {
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