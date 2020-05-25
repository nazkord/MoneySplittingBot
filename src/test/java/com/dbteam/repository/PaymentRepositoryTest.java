package com.dbteam.repository;

import com.dbteam.model.Payment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentRepositoryTest {

    @Autowired PaymentRepository paymentRepository;

    @BeforeAll()
    public void setUp() {

        Payment payment1 = new Payment(1L,1L,LocalDateTime.of(2000, 12, 27, 0, 0),"Vasia", 10D, "Pronia", true);
        Payment payment2 = new Payment(2L,1L,LocalDateTime.of(2010, 1, 1, 0, 0),"Pronia", 20D, "Vasia", false);
        Payment payment3 = new Payment(3L,1L,LocalDateTime.of(2015, 12, 27, 0, 0),"Vasia", 30D, "Pronia", false);
        Payment payment4 = new Payment(4L,1L,LocalDateTime.of(2017, 12, 27, 0, 0), "Pronia", 40D, "Vasia", true);
        Payment payment5 = new Payment(5L,2L,LocalDateTime.of(2020, 12, 27, 0, 0), "Vasia", 50D, "Pronia", true);
        Payment payment6 = new Payment(6L,2L,LocalDateTime.of(2090, 12, 27, 0, 0), "Pronia", 60D, "Vasia", false);
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

    @Test
    public void getPaymentsByGroupChatIdAndIsConfirmedIsTrue() {
        // when
        List<Payment> payments = paymentRepository.getPaymentsByGroupChatIdAndIsConfirmedIsTrue(1L);

        // then
        assertEquals(2, payments.size());
        assertEquals(10D, payments.get(0).getAmount());

    }

    @Test
    public void getPaymentsByGroupChatIdAndIsConfirmedIsFalse() {
        // when
        List<Payment> payments = paymentRepository.getPaymentsByGroupChatIdAndIsConfirmedIsFalse(1L);

        // then
        assertEquals(2, payments.size());
        assertEquals(20D, payments.get(0).getAmount());
    }

    @Test
    public void getFirstPaymentWithUserAfter() {

        // when
        Payment payment = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateGreaterThanOrderByDateDesc("Pronia",
                        "Pronia", LocalDateTime.of(2020, 12, 27, 0 ,0)).get(0);

        //then
        assert payment != null;
        assertEquals(2090, payment.getDate().getYear());
        assertEquals(12, payment.getDate().getMonth().getValue());
        assertEquals(27, payment.getDate().getDayOfMonth());
    }

    @Test
    public void getFirstPaymentWithUserBefore () {
        // when
        Payment payment = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateLessThanOrderByDateDesc("Pronia",
                        "Pronia",
                        LocalDateTime.of(2090, 12, 27, 0, 0)).get(0);

        // then
        assert payment != null;
        assertEquals(2020, payment.getDate().getYear());
        assertEquals(12, payment.getDate().getMonth().getValue());
        assertEquals(27, payment.getDate().getDayOfMonth());
    }


    @Test
    public void getFirstPaymentWithUserBeforePayment () {
        //given
        Payment payment1 = new Payment(
                1L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 1),
                "jan",
                1D,
                "kuba",
                false);
        Payment payment2 = new Payment(
                2L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 2),
                "kuba",
                1D,
                "jan",
                false);
        Payment payment3 = new Payment(
                3L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 3),
                "jan",
                1D,
                "kuba",
                false);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        // when
        List<Payment> payments = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateLessThanOrderByDateDesc(
                        "kuba",
                        "kuba",
                        payment3.getDate());
        Payment payment = payments.get(1);

        // then
        assert payment != null;
        assertEquals(2, payment.getDate().getMinute());
    }

    @Test
    public void getFirstPaymentWithUserAfterPayment () {
        //given
        Payment payment1 = new Payment(
                1L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 1),
                "jan",
                1D,
                "kuba",
                false);
        Payment payment2 = new Payment(
                2L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 2),
                "kuba",
                1D,
                "jan",
                false);
        Payment payment3 = new Payment(
                3L,
                0L,
                LocalDateTime.of(2000, 1, 1,0, 3),
                "jan",
                1D,
                "kuba",
                false);
        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        // when
        Payment payment = paymentRepository
                .getByRecipientEqualsOrPayerEqualsAndDateGreaterThanOrderByDateDesc(
                        "kuba",
                        "kuba",
                        payment1.getDate()).get(1);

        // then
        assert payment != null;
        assertEquals(2, payment.getDate().getMinute());
    }

}