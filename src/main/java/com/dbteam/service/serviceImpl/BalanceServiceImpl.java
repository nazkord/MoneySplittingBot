package com.dbteam.service.serviceImpl;

import com.dbteam.exception.GroupNotFoundException;
import com.dbteam.exception.PersonNotFoundException;
import com.dbteam.model.Group;
import com.dbteam.model.Payment;
import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import com.dbteam.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final PurchaseService purchaseService;
    private final PaymentService paymentService;
    private final PersonService personService;
    private final GroupService groupService;

    private Map<String, Double> balanceMap;

    private Person targetPerson;

    private Group targetGroup;

    public BalanceServiceImpl(PurchaseService purchaseService, PaymentService paymentService, PersonService personService, GroupService groupService) {
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
        this.personService = personService;
        this.groupService = groupService;
    }

    @Override
    public Map<String, Double> getBalanceMap(String username, Long groupChatId)
            throws PersonNotFoundException, GroupNotFoundException {
        balanceMap = new HashMap<>();

        // Fetching user and group
        try {
            targetPerson = personService.findPersonByUsername(username);
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            throw new PersonNotFoundException("No person with such username!");
        }
        try {
            targetGroup = groupService.findGroupById(groupChatId);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
            throw new GroupNotFoundException("No group with such groupChatId!");
        }

        List<Person> groupMembers = targetGroup.getPeople();

        // Initializing of balance map
        groupMembers.forEach( member -> {
            if (!member.getUsername().equals(username)) {

                balanceMap.put(member.getUsername(), 0D);
            }
        });

        // Applying different transactions
        applyOutPurchases();

        applyInPurchases();

        applyOutPayments();

        applyInPayments();

        return balanceMap;
    }

    private void applyInPayments() {
        List<Payment> payments = paymentService
                .getConfirmedPaymentsWithRecipient(
                        targetPerson.getUsername(),
                        targetGroup.getGroupChatId());

        payments.forEach( payment -> {

            double currentBalance = balanceMap.get(payment.getPayer())
                    - payment.getAmount();

            balanceMap.replace(payment.getPayer(), currentBalance);
        });
    }

    private void applyOutPayments() {
        List<Payment> payments = paymentService
                .getConfirmedPaymentsWithPayer(
                        targetPerson.getUsername(),
                        targetGroup.getGroupChatId());

        payments.forEach(payment -> {

            double currentBalance = balanceMap
                    .get(payment.getRecipient()) + payment.getAmount();

            balanceMap.replace(payment.getRecipient(), currentBalance);

        });
    }

    private void applyInPurchases() {
        List<Purchase> purchases = purchaseService
                .getPurchasesWithReceiver(
                        targetGroup.getGroupChatId(), targetPerson);

       purchases.forEach(purchase -> {

            double dividedPrice =
                    purchase.getAmount() / purchase.getRecipients().size();

            double currentBalance = balanceMap.get(purchase.getBuyer()) - dividedPrice;

            balanceMap.replace(purchase.getBuyer(), currentBalance);
        });
    }

    private void applyOutPurchases() {
        List<Purchase> purchases = purchaseService
                .getPurchasesWithBuyer(targetGroup.getGroupChatId(), targetPerson);

        purchases.forEach(purchase -> {

            List<Person> recipients = purchase.getRecipients();

            double dividedPrice = purchase.getAmount() / recipients.size();

            recipients.forEach(person -> {
                double currentBalance = balanceMap.get(person.getUsername())
                        + dividedPrice;
                balanceMap.replace(person.getUsername(), currentBalance);
            });
        });
    }

}
