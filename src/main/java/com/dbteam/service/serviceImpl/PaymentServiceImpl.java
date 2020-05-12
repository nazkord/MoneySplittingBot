package com.dbteam.service.serviceImpl;

import com.dbteam.model.Payment;
import com.dbteam.repository.PaymentRepository;
import com.dbteam.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void addPayment(Payment payment) {
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getConfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId) {
        return paymentRepository
                .getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId(
                        recipientUsername,
                        groupChatId);

    }

    @Override
    public List<Payment> getUnconfirmedPaymentsWithRecipient(String recipientUsername, Long groupChatId) {
        return paymentRepository
                .getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId(
                        recipientUsername,
                        groupChatId);
    }

    @Override
    public List<Payment> getConfirmedPaymentsWithPayer(String payerUsername, Long groupChatId) {
        return paymentRepository
                .getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId(
                        payerUsername,
                        groupChatId);
    }

    @Override
    public List<Payment> getUnconfirmedPaymentsWithPayer(String payerUsername, Long groupChatId) {
        return paymentRepository
                .getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId(
                        payerUsername,
                        groupChatId);
    }

    @Override
    public List<Payment> getConfirmedPaymentsOfGroup(Long groupChatId) {
        return paymentRepository
                    .getPaymentsByGroupChatIdAndIsConfirmedIsTrue(groupChatId);

    }

    @Override
    public List<Payment> getUnconfirmedPaymentsOfGroup(Long groupChatId) {
        return paymentRepository
                .getPaymentsByGroupChatIdAndIsConfirmedIsFalse(groupChatId);
    }

    @Override
    public Payment getFirstPaymentWithUserBefore(LocalDate date, String username) {
        Optional<Payment> payment = paymentRepository
                .getPaymentByRecipientEqualsOrPayerEqualsAndDateBeforeOrderByDateDesc(
                        username, username, date).findFirst();
        return payment.orElse(null);
    }

    @Override
    public Payment getFirstPaymentWithUserAfter(LocalDate date, String username) {
        Optional<Payment> payment = paymentRepository
                .getPaymentByRecipientEqualsOrPayerEqualsAndDateAfterOrderByDateDesc(
                        username, username, date).findFirst();
        return payment.orElse(null);
    }
}
