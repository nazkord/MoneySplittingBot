package com.dbteam.repository;

import com.dbteam.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, Long> {
    List<Payment> getPaymentsByRecipientAndIsConfirmedIsFalse(String recipientUsername);
}