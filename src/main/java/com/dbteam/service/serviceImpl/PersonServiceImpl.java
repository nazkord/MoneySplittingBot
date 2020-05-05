package com.dbteam.service.serviceImpl;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Person;
import com.dbteam.repository.PersonRepository;
import com.dbteam.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void addPerson(Person person) {
        personRepository.save(person);
    }

    @Override
    public void updatePerson(Person person) {
        Person newPerson = new Person();
        person.saveTo(newPerson);
        personRepository.save(newPerson);
    }

    @Override
    public Person findPersonByUsername(String username) throws IllegalUsernameException {
        Optional<Person> optionalPerson = personRepository.findUserByUsername(username);
        return optionalPerson.orElseThrow(() -> new IllegalUsernameException("There is no person in db with username: " + username));
    }

    @Override
    public void updatePersonBotChatState(String username, String newState) throws IllegalUsernameException {
        Person person = findPersonByUsername(username);
        person.setBotChatState(newState);
        updatePerson(person);
    }

    @Override
    public void updatePersonGroupChatState(String username, String newState) throws IllegalUsernameException {
        Person person = findPersonByUsername(username);
        person.setGroupChatState(newState);
        updatePerson(person);
    }
}
