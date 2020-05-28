package com.dbteam.repository;

import com.dbteam.model.db.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    List<Payment> getByRecipientEqualsOrPayerEqualsAndDateLessThanOrderByDateDesc(String recipientUsername, String payerUsername, LocalDateTime date);

    /**
     * Gets first payment after date with such payer or receiver
     */
    List<Payment> getByRecipientEqualsOrPayerEqualsAndDateGreaterThanOrderByDateAsc(String recipientUsername, String payerUsername, LocalDateTime date);

    /**
     * Gets payment with payment id.
     */
    Optional<Payment> findFirstByPaymentId(Long paymentId);

}