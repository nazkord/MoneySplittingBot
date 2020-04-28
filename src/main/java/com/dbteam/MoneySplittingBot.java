package com.dbteam;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Group;
import com.dbteam.model.Person;
import com.dbteam.repository.GroupRepository;
import com.dbteam.repository.PersonRepository;
import com.dbteam.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.Optional;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;

@Component
public class MoneySplittingBot extends AbilityBot {

    private final int creatorId;

    @Autowired
    PersonService personService;

    @Autowired
    GroupRepository groupRepository;

    public MoneySplittingBot(BotConfiguration botConfiguration) {
        super(botConfiguration.getBotToken(), botConfiguration.getBotName());
        creatorId = botConfiguration.getCreatorId();
    }

    @Override
    public int creatorId() {
        return creatorId;
    }

    public Ability sayHelloWorld() {
        String playMessage = "check";

        return Ability
                .builder()
                .name("hello")
                .info("says hello world!")
                .input(0)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    User telegramUser = ctx.update().getMessage().getFrom();
                    personService.updatePerson(new Person(telegramUser.getUserName(),
                            telegramUser.getFirstName() + " " + telegramUser.getLastName(),
                            ctx.chatId(), ctx.chatId(), null, null));
                    try {
                        Person person = personService.findPersonByUsername(telegramUser.getUserName());
//                        groupRepository.save(new Group(ctx.chatId(), Collections.singletonList(person)));
                        silent.send(person.getFullName(), ctx.chatId());
                    } catch (IllegalUsernameException e) {
                        silent.send("There is no such user in db", ctx.chatId());
                    }
                })
                .build();
    }
    
}
