package com.dbteam.repository;

import com.dbteam.model.Payment;
import com.dbteam.model.Purchase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentRepositoryTest {

    @Autowired PaymentRepository paymentRepository;

    @BeforeAll()
    public void setUp() {
        Payment payment1 = new Payment(1L,5L,"Vasia", 10D, "Pronia", true);
        Payment payment2 = new Payment(2L,5L,"Pronia", 20D, "Vasia", false);
        Payment payment3 = new Payment(3L,5L,"Vasia", 30D, "Pronia", false);
        Payment payment4 = new Payment(4L,5L,"Pronia", 40D, "Vasia", true);
        Payment payment5 = new Payment(5L,5L,"Vasia", 50D, "Pronia", true);
        paymentRepository.saveAll(List.of(payment1, payment2, payment3, payment4, payment5));
    }

    @Test
    public void getPaymentsByRecipientAndIsConfirmedIsFalse() {
        //when
        List<Payment> list = paymentRepository.getPaymentsByRecipientAndIsConfirmedIsFalse("Pronia");

        //then
        assertEquals(1, list.size());
        assertEquals(30D, list.get(0).getAmount());
    }

    @Test
    public void getPaymentsByRecipientAndIsConfirmedIsTrue() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByRecipientAndIsConfirmedIsTrue("Vasia");

        //then
        assertEquals(1, payments.size());
        assertEquals(40D, payments.get(0).getAmount());
    }

    @Test
    public void getPaymentsByPayerAndIsConfirmedIsTrue() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByPayerAndIsConfirmedIsTrue("Vasia");

        //then
        assertEquals(2, payments.size());
        Double amounts = payments
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
        assertEquals(60D, amounts);
    }

    @Test
    public void getPaymentsByPayerAndIsConfirmedIsFalse() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByPayerAndIsConfirmedIsFalse("Pronia");

        //then
        assertEquals(1, payments.size());
        assertEquals(20D, payments.get(0).getAmount());
    }
}