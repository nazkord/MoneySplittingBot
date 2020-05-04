package com.dbteam.service.serviceImpl;

import com.dbteam.exception.IllegalUsernameException;
import com.dbteam.model.Payment;
import com.dbteam.model.Person;
import com.dbteam.model.Purchase;
import com.dbteam.service.BalanceService;
import com.dbteam.service.PaymentService;
import com.dbteam.service.PersonService;
import com.dbteam.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PersonService personService;

    private Map<String, Double> balanceMap;

    private Person targetPerson;

    private Long groupChatId;

    @Override
    public Map<String, Double> getBalanceMap(String username, Long groupChatId) {
        balanceMap = new HashMap<>();
        try {
            targetPerson = personService.findPersonByUsername(username);
        } catch (IllegalUsernameException e) {
            e.printStackTrace();
            return null;
        }
        this.groupChatId = groupChatId;

        List<Person> groupMembers = personService.getPersonsOfGroup(groupChatId);

        for (Person member: groupMembers) {
            if (member.getUsername().equals(username)) continue;

            balanceMap.put(member.getUsername(), 0D);
        }

        applyOutPurchases();

        applyInPurchases();

        applyOutPayments();

        applyInPayments();

        return balanceMap;
    }

    private void applyInPayments() {
        List<Payment> payments = paymentService
                .getPaymentsWithRecipient(
                        targetPerson.getUsername(),
                        groupChatId,
                        true);

        for (Payment payment: payments) {

            double currentBalance = balanceMap.get(payment.getPayer())
                    - payment.getAmount();

            balanceMap.replace(payment.getPayer(), currentBalance);
        }
    }

    private void applyOutPayments() {
        List<Payment> payments = paymentService
                .getPaymentsWithPayer(
                        targetPerson.getUsername(),
                        groupChatId,
                        true);

        for (Payment payment: payments) {

            double currentBalance = balanceMap
                    .get(payment.getRecipient()) + payment.getAmount();

            balanceMap.replace(payment.getRecipient(), currentBalance);

        }
    }

    private void applyInPurchases() {
        List<Purchase> purchases = purchaseService
                .getPurchasesWithReceiver(groupChatId, targetPerson);

        for (Purchase purchase: purchases) {

            double dividedPrice =
                    purchase.getAmount() / purchase.getRecipients().size();

            double currentBalance = balanceMap.get(purchase.getBuyer()) - dividedPrice;

            balanceMap.replace(purchase.getBuyer(), currentBalance);
        }
    }

    private void applyOutPurchases() {
        List<Purchase> purchases = purchaseService
                .getPurchasesWithBuyer(groupChatId, targetPerson);

        for (Purchase purchase: purchases) {
            List<Person> recipients = purchase.getRecipients();

            double dividedPrice = purchase.getAmount() / recipients.size();

            for(Person person : recipients) {
                double currentBalance = balanceMap.get(person.getUsername())
                        + dividedPrice;
                balanceMap.replace(person.getUsername(), currentBalance);
            }
        }
    }

}
