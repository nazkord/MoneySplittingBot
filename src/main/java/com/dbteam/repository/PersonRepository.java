package com.dbteam.repository;

import com.dbteam.model.Person;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends MongoRepository<Person, String> {
    Optional<Person> findUserByUsername(String username);
}