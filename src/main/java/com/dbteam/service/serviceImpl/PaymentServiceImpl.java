package com.dbteam.service.serviceImpl;

import com.dbteam.exception.PaymentNotFoundException;
import com.dbteam.model.db.Payment;
import com.dbteam.repository.PaymentRepository;
import com.dbteam.service.PaymentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public void updatePayment(Payment payment) {
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
    public Payment getFirstPaymentWithUserBefore(LocalDateTime date, String username) throws PaymentNotFoundException {
        List<Payment> list = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateLessThanOrderByDateDesc(
                        username, username, date);
        for (Payment payment: list) {
            if (payment.getDate().isBefore(date)) return payment;
        }
        return null;
    }

    @Override
    public Payment getFirstPaymentWithUserAfter(LocalDateTime date, String username) throws PaymentNotFoundException {
        List<Payment> list = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateGreaterThanOrderByDateAsc(
                        username, username, date);
        for (Payment payment: list) {
            if (payment.getDate().isAfter(date)) return payment;
        }
        return null;
    }

    @Override
    public Payment getFirstPaymentWithUserBefore(Long paymentId, String username) throws PaymentNotFoundException {
        Payment payment = paymentRepository
                .findFirstByPaymentId(paymentId).orElseThrow(new PaymentNotFoundException());

        return getFirstPaymentWithUserBefore(payment.getDate(), username);
    }

    @Override
    public Payment getFirstPaymentWithUserAfter(Long paymentId, String username) throws PaymentNotFoundException {
        Payment payment = paymentRepository
                .findFirstByPaymentId(paymentId).orElseThrow(new PaymentNotFoundException());

        return getFirstPaymentWithUserAfter(payment.getDate(), username);
    }

    @Override
    public Payment getPaymentById(Long paymentId) throws PaymentNotFoundException {
        return paymentRepository.findFirstByPaymentId(paymentId)
                .orElseThrow(new PaymentNotFoundException());
    }
}
