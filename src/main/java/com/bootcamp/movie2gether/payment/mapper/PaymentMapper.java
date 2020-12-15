package com.bootcamp.movie2gether.payment.mapper;

import com.bootcamp.movie2gether.payment.dto.PaymentRequest;
import com.bootcamp.movie2gether.payment.dto.PaymentResponse;
import com.bootcamp.movie2gether.payment.entity.Payment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public Payment toEntity(PaymentRequest paymentRequest) {
        Payment payment = new Payment();

        BeanUtils.copyProperties(paymentRequest,payment);
        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {
        PaymentResponse paymentResponse = new PaymentResponse();
        BeanUtils.copyProperties(payment, paymentResponse);
        return paymentResponse;
    }
}
