package com.bootcamp.movie2gether.payment.integration;

import com.bootcamp.movie2gether.payment.repository.PaymentRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegration {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final String apiBaseUrl = "/payments";

    @AfterEach
    void tearDown() {
        this.paymentRepository.deleteAll();
    }
}
