package com.bootcamp.movie2gether.payment.service;

import com.bootcamp.movie2gether.payment.entity.Payment;
import com.bootcamp.movie2gether.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public Payment createPayment(Payment newPayment) {
        return this.paymentRepository.insert(newPayment);
    }
}
