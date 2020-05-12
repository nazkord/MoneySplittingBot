package com.dbteam.service;

import com.dbteam.model.Payment;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    void addPayment(Payment payment);

    List<Payment> getConfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId);

    List<Payment> getUnconfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId);

    List<Payment> getConfirmedPaymentsWithPayer(String payerUsername, Long groupChatId);

    List<Payment> getUnconfirmedPaymentsWithPayer(String payerUsername, Long groupChatId);

    List<Payment> getConfirmedPaymentsOfGroup(Long groupChatId);

    List<Payment> getUnconfirmedPaymentsOfGroup(Long groupChatId);

    Payment getFirstPaymentWithUserBefore(LocalDate date, String username);

    Payment getFirstPaymentWithUserAfter(LocalDate date, String username);

}
