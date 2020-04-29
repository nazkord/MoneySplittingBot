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
        Payment payment1 = new Payment(1L,1L,"Vasia", 10D, "Pronia", true);
        Payment payment2 = new Payment(2L,1L,"Pronia", 20D, "Vasia", false);
        Payment payment3 = new Payment(3L,1L,"Vasia", 30D, "Pronia", false);
        Payment payment4 = new Payment(4L,1L,"Pronia", 40D, "Vasia", true);
        Payment payment5 = new Payment(5L,2L,"Vasia", 50D, "Pronia", true);
        Payment payment6 = new Payment(6L,2L,"Pronia", 60D, "Vasia", false);
        paymentRepository.saveAll(List.of(payment1, payment2, payment3, payment4, payment5, payment6));
    }

    @Test
    public void getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId() {
        //when
        List<Payment> list = paymentRepository.getPaymentsByRecipientAndIsConfirmedIsFalseAndGroupChatId("Pronia", 1L);

        //then
        assertEquals(1, list.size());
        assertEquals(30D, list.get(0).getAmount());
    }

    @Test
    public void getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByRecipientAndIsConfirmedIsTrueAndGroupChatId("Vasia", 1L);

        //then
        assertEquals(1, payments.size());
        assertEquals(40D, payments.get(0).getAmount());
    }

    @Test
    public void getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByPayerAndIsConfirmedIsTrueAndGroupChatId("Vasia", 2L);

        //then
        assertEquals(1, payments.size());
        assertEquals(50D, payments.get(0).getAmount());
    }

    @Test
    public void getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId() {
        //when
        List<Payment> payments = paymentRepository.getPaymentsByPayerAndIsConfirmedIsFalseAndGroupChatId("Pronia", 2L);

        //then
        assertEquals(1, payments.size());
        assertEquals(60D, payments.get(0).getAmount());
    }
}