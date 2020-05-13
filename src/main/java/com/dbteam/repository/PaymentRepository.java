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
     * Gets first payment before date with such payer or receiver
     */
    Optional<Payment> getFirstByRecipientEqualsOrPayerEqualsAndDateBeforeOrderByDateDesc(String recipientUsername, String payerUsername, LocalDate date);

    /**
     * Gets first payment after date with such payer or receiver
     */
    Optional<Payment> getFirstByRecipientEqualsOrPayerEqualsAndDateAfterOrderByDateDesc(String recipientUsername, String payerUsername, LocalDate date);

}