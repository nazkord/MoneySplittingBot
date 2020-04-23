package com.dbteam.repository;

import com.dbteam.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PaymentRepository extends MongoRepository<Payment, Long> {
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsFalse(String recipientUsername);
}