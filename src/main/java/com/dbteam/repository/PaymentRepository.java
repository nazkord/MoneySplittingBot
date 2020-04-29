package com.dbteam.repository;

import com.dbteam.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Long> {
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId(String recipientUsername, Long groupChatId);
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId(String recipientUsername, Long groupChatId);
    List<Payment> getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId(String payerUsername, Long groupChatId);
    List<Payment> getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId(String payerUsername, Long groupChatId);
}