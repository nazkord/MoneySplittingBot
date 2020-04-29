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
    public List<Payment> getPaymentsWithRecipient(String recipientUsername, Long groupChatId, boolean isConfirmed) {
        if (isConfirmed)
            return paymentRepository
                    .getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId(
                            recipientUsername,
                            groupChatId);
        else
            return paymentRepository
                .getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId(
                    recipientUsername,
                    groupChatId);
    }

    @Override
    public List<Payment> getPaymentsWithPayer(String payerUsername, Long groupChatId, boolean isConfirmed) {
        if (isConfirmed)
            return paymentRepository
                    .getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId(
                            payerUsername,
                            groupChatId);
        else
            return paymentRepository
                    .getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId(
                            payerUsername,
                            groupChatId);
    }

    @Override
    public List<Payment> getPaymentsOfGroup(Long groupChatId, boolean isConfirmed) {
        if (isConfirmed)
            return paymentRepository
                    .getPaymentsByGroupChatIdAndIsConfirmedIsTrue(groupChatId);
        else
            return paymentRepository
                    .getPaymentsByGroupChatIdAndIsConfirmedIsFalse(groupChatId);
    }


}
