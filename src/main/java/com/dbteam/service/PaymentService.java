package com.dbteam.service;

import com.dbteam.model.Payment;

import java.util.List;

public interface PaymentService {
    void addPayment(Payment payment);

    List<Payment> getPaymentsWithRecipient(String recipientUsername, Long groupChatId, boolean isConfirmed);

    List<Payment> getPaymentsWithPayer(String payerUsername, Long groupChatId, boolean isConfirmed);

    List<Payment> getPaymentsOfGroup(Long groupChatId, boolean isConfirmed);

}
