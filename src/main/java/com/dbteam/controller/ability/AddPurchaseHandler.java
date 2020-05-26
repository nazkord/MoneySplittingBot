package com.dbteam.controller.ability;

import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Command;
import com.dbteam.model.Payment;
import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import com.dbteam.service.*;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AddPurchaseHandler implements CommandHandler{

    private static final String MSG_PRIMARY =
            "Okay. Adding new purchase. What did you buy?";
    private static final String MSG_AMOUNT =
            "Saved That. What is the total price?";
    private static final String MSG_AMOUNT_ERROR =
            "This doesn't look like a valid price :/ " +
            "Please try again.";
    private static final String MSG_RECIPIENTS =
            "Got it. Who should split this purchase with you?";
    private static final String MSG_NO_RECIPIENTS =
            "There are no valid recipients. Try again ;)";
    private static final String MSG_RECIPIENT_DOESNT_BELONG =
            "User @%s does not belong to the group!";
    private static final String MSG_SUCCESS =
            "I saved this purchase. If you would like to see your balance type /checkbalance.";
    private static final String MSG_USER_IN_RECIPIENTS =
            "You added yourself as a recipient. " +
            "No worries, I will split evenly.";

    private static final String STATE_DESCRIPTION = "description";
    private static final String STATE_AMOUNT = "amount";
    private static final String STATE_RECIPIENTS = "recipients";
    private static final String NO_STATE = "noState";
    private static final String PERSON_NOT_FOUND = "personNotFound";
    private static final String PERSON_DOESNT_BELONG = "personDoesntBelong";
    private static final String PERSON_OK = "personOk";
    private static final String MSG_ONLY_USER_IN_RECIPIENTS =
            "You bought it only for yourself?";

    private final PersonService personService;
    private final PurchaseService purchaseService;
    private final StateService stateService;
    private final SequenceGeneratorService sequenceGeneratorService;


    private Update currentUpdate;
    private Purchase currentPurchase;
    private Person currentPerson;
    private String currentState;

    public AddPurchaseHandler(PersonService personService,
                              PurchaseService purchaseService,
                              StateService stateService, SequenceGeneratorService sequenceGeneratorService) {
        this.personService = personService;
        this.purchaseService = purchaseService;
        this.stateService = stateService;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public List<BotApiMethod<?>> primaryAction(Update update) {
        // create a message with primary text
        SendMessage message = new SendMessage(
                update.getMessage().getChatId(),
                MSG_PRIMARY);
        // create, save new purchase and remember purchaseId
        Purchase purchase = new Purchase(
                sequenceGeneratorService.generateSequence(Payment.SEQUENCE_NAME),
                update.getMessage().getChatId(),
                update.getMessage().getFrom().getUserName(),
                LocalDate.now(),
                "",
                null,
                "",
                null);
        long purchaseId = purchaseService.savePurchase(purchase);
        // change person's group chat state
        updatePersonGroupChatState(update, purchaseId, STATE_DESCRIPTION);
        // return message
        return List.of(message);
    }

    private void updatePersonGroupChatState(Update update, Long purchaseId, String state) {
        String statePrefix;
        if(state.equals(NO_STATE)) {
            statePrefix = "";
        } else {
            statePrefix = Command.ADD_PURCHASE.getValue() + "/" + purchaseId.toString();
        }
        personService.updatePersonGroupChatState(
                getUsername(update),
                stateService.buildBotChatState(statePrefix, state),
                update.getMessage().getChatId()
        );
    }

    @Override
    public List<BotApiMethod<?>> secondaryAction(Update update) {
        // load current attributes (purchase, person)
        currentUpdate = update;
        loadCurrentAttributes();

        if (userResetCommand()) {
            discardCurrentProgress();
            return primaryAction(update);
        }

        // depending on last part of state switch cases
        String expectedInput = currentState.split("/")[2];
        switch(expectedInput) {
            case STATE_DESCRIPTION:
                return saveDescription();
            case STATE_AMOUNT:
                return saveAmount();
            case STATE_RECIPIENTS:
                return saveRecipients();
            default:
                return new ArrayList<>();
        }
    }

    private boolean userResetCommand() {
        String text = currentUpdate.getMessage().getText();
        return text.startsWith("/"+Command.ADD_PURCHASE.getValue().toLowerCase());
    }

    private void discardCurrentProgress() {
        purchaseService.remove(currentPurchase);
    }

    private List<BotApiMethod<?>> saveDescription() {
        String description = currentUpdate.getMessage().getText();
        currentPurchase.setDescription(description);
        purchaseService.savePurchase(currentPurchase);  // saving current progress
        SendMessage message = new SendMessage();
        message.setText(MSG_AMOUNT);
        message.setChatId(currentUpdate.getMessage().getChatId());
        updatePersonGroupChatState(currentUpdate, currentPurchase.getPurchaseId(), STATE_AMOUNT);
        return List.of(message);
    }

    private List<BotApiMethod<?>> saveAmount() {
        double amount;
        try {
            amount = Double.parseDouble(currentUpdate.getMessage().getText());
        } catch(Exception e) {
            e.printStackTrace();
            SendMessage message = new SendMessage();
            message.setText(MSG_AMOUNT_ERROR);
            message.setChatId(currentUpdate.getMessage().getChatId());
            return List.of(message);
        }
        currentPurchase.setAmount(amount);
        purchaseService.savePurchase(currentPurchase);  // saving current progress
        SendMessage message = new SendMessage();
        message.setText(MSG_RECIPIENTS);
        message.setChatId(currentUpdate.getMessage().getChatId());
        updatePersonGroupChatState(currentUpdate, currentPurchase.getPurchaseId(), STATE_RECIPIENTS);
        return List.of(message);
    }

    private List<BotApiMethod<?>> saveRecipients() {
        List<String> potentialRecipients = new ArrayList<>(getParsedUsernames(currentUpdate.getMessage().getText()));
        Map<String, String> validityMap = validateRecipients(potentialRecipients);

        List<BotApiMethod<?>> errorMessages = new ArrayList<>();
        List<Person> recipients = new ArrayList<>();
        validityMap.forEach((username, validity) -> {
            if (!validity.equals(PERSON_OK)) {
                errorMessages.add(new SendMessage(
                        currentUpdate.getMessage().getChatId(),
                        String.format(MSG_RECIPIENT_DOESNT_BELONG, username)
                ));
            } else {
                try {
                    recipients.add(personService.findPersonByUsername(username));
                } catch (PersonNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        if (!errorMessages.isEmpty()) {
            return errorMessages;
        }

        List<BotApiMethod<?>> apiMethods = new ArrayList<>();
        if (recipients.contains(currentPerson)) {
            excludeCurrentPersonFromList(recipients);
            apiMethods.add(
                    new SendMessage(
                            currentUpdate.getMessage().getChatId(),
                            MSG_USER_IN_RECIPIENTS
                    )
            );
            if (recipients.isEmpty()) {
                return List.of(
                        new SendMessage(
                                currentUpdate.getMessage().getChatId(),
                                MSG_ONLY_USER_IN_RECIPIENTS
                        )
                );
            }
        }
        if (recipients.isEmpty()) {
            return List.of(
                    new SendMessage(
                            currentUpdate.getMessage().getChatId(),
                            MSG_NO_RECIPIENTS
                    )
            );
        }

        currentPurchase.setRecipients(recipients);
        purchaseService.savePurchase(currentPurchase);  // saving ready purchase
        SendMessage message = new SendMessage(
                currentUpdate.getMessage().getChatId(),
                MSG_SUCCESS
        );
        updatePersonGroupChatState(currentUpdate, null, NO_STATE);
        apiMethods.add(message);
        return apiMethods;
    }

    private List<String> getParsedUsernames(String rawUsernames) {
        String[] parsedUsernames = rawUsernames.toLowerCase().trim()
                .replace(" ", "").split("@");
        List<String> result = new ArrayList<>();
        for (String username : parsedUsernames) {
            if (!username.isEmpty() && !username.isBlank()) {
                result.add(username);
            }
        }
        return result;
    }

    private Map<String, String> validateRecipients(List<String> recipients) {
        return recipients.stream()
                .collect(Collectors.toMap(Function.identity(), username -> {
                    Person person;
                    try {
                        person = personService
                                .findPersonByUsername(username);
                    } catch (PersonNotFoundException e) {
                        e.printStackTrace();
                        return PERSON_NOT_FOUND;
                    }
                    if (!recipientIsInGroup(person)) {
                        return PERSON_DOESNT_BELONG;
                    }
                    return PERSON_OK;
                }));
    }



    private boolean recipientIsInGroup(Person person) {
        return person.getGroupChatsStates().containsKey(currentPurchase.getGroupChatId());
    }

    private void excludeCurrentPersonFromList(List<Person> recipients) {
        recipients.remove(currentPerson);
    }

    private void loadCurrentAttributes() {
        try {
            currentPerson = personService.findPersonByUsername(getUsername(currentUpdate));
            currentState = personService.getPersonGroupChatState(currentPerson.getUsername(), currentUpdate.getMessage().getChatId());
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        Long purchaseId = Long.parseLong(currentState.split("/")[1]);
        currentPurchase = purchaseService.getPurchaseById(purchaseId);
    }

    public Command commandToHandle() {
        return Command.ADD_PURCHASE;
    }

}
