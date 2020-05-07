package com.dbteam.service.serviceImpl;

import com.dbteam.model.Payment;
import com.dbteam.repository.PaymentRepository;
import com.dbteam.repository.PersonRepository;
import com.dbteam.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

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
}
