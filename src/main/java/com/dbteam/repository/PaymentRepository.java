package com.dbteam.repository;

import com.dbteam.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Long> {
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId(String recipientUsername, Long groupChatId);
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId(String recipientUsername, Long groupChatId);
    List<Payment> getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId(String payerUsername, Long groupChatId);
    List<Payment> getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId(String payerUsername, Long groupChatId);
    /**
     * Gets all confirmed payments of a specific group.
     */
    List<Payment> getPaymentsByGroupChatIdAndIsConfirmedIsTrue(Long groupChatId);

    /**
     * Gets all unconfirmed payments of a specific group
     */
    List<Payment> getPaymentsByGroupChatIdAndIsConfirmedIsFalse(Long groupChatId);

    /**
     * Gets stream of payments sorted desc before date
     */
    Stream<Payment> getPaymentByRecipientEqualsOrPayerEqualsAndDateBeforeOrderByDateDesc(String recipientUsername, String payerUsername, LocalDate date);

    /**
     * Gets stream of payments sorted desc after date
     */
    Stream<Payment> getPaymentByRecipientEqualsOrPayerEqualsAndDateAfterOrderByDateDesc(String recipientUsername, String payerUsername, LocalDate date);


}