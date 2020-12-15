package com.bootcamp.movie2gether.payment.service;

import com.bootcamp.movie2gether.payment.entity.Payment;
import com.bootcamp.movie2gether.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {
    @InjectMocks
    private PaymentService paymentService;
    @Mock
    private PaymentRepository paymentRepository;
    @Test
    public void should_return_new_payment_when_create_payment_given_new_payment() {
        //given

        Payment expected = new Payment("Peter","12341234",true,"Master","123123");
        //when
        when(paymentRepository.insert(expected)).thenReturn(expected);

        //then
        Payment actual = paymentService.createPayment(expected);
        assertEquals(expected,actual);
    }

}
