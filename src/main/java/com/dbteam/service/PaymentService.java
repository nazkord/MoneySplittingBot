package com.dbteam.service;

import com.dbteam.exception.PaymentNotFoundException;
import com.dbteam.model.Payment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    void addPayment(Payment payment);

    void updatePayment(Payment payment);

    List<Payment> getConfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId);

    List<Payment> getUnconfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId);

    List<Payment> getConfirmedPaymentsWithPayer(String payerUsername, Long groupChatId);

    List<Payment> getUnconfirmedPaymentsWithPayer(String payerUsername, Long groupChatId);

    List<Payment> getConfirmedPaymentsOfGroup(Long groupChatId);

    List<Payment> getUnconfirmedPaymentsOfGroup(Long groupChatId);

    Payment getFirstPaymentWithUserBefore(LocalDateTime date, String username) throws PaymentNotFoundException;

    Payment getFirstPaymentWithUserAfter(LocalDateTime date, String username) throws PaymentNotFoundException;

    Payment getFirstPaymentWithUserBefore(Long paymentId, String username) throws PaymentNotFoundException;

    Payment getFirstPaymentWithUserAfter(Long paymentId, String username) throws PaymentNotFoundException;

    Payment getPaymentById(Long paymentId) throws PaymentNotFoundException;
}
