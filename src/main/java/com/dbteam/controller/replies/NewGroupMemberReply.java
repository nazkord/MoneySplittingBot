package com.dbteam.controller.replies;

import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NewGroupMemberReply extends BasicReply{

//    @Autowired
//    private GroupService groupService;


    public NewGroupMemberReply(SilentSender silent, AbilityBot bot) {
        super(silent, bot);
    }

    public Reply get() {

        return Reply.of(action, conditions);

    }

    private final Consumer<Update> action = (update -> {

        AnswerCallbackQuery answer = new AnswerCallbackQuery();
        answer
                .setText("Successfully added.")
                .setCallbackQueryId(update.getCallbackQuery().getId());

        try {
            bot.execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    });

//    private void savePerson(Message msg) {
//        User u = msg.getFrom();
//        try {
//            groupService.addUserToGroup(
//                    msg.getChatId(),
//                    new Person(u.getUserName(),
//                            u.getFirstName()+" "+u.getLastName(),
//                            msg.getChatId(), null,
//                            null, null));
//        } catch (PersonNotFoundException | GroupNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    private final List<Predicate<Update>> conditions = List.of(
            (update) -> {
                if (update.hasCallbackQuery()) {
                    return update.getCallbackQuery().getData().equals("new_member");
                }
                return false;
            }
    );

}
