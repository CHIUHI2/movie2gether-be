package com.bootcamp.movie2gether.payment.controller;
import com.bootcamp.movie2gether.payment.dto.PaymentRequest;
import com.bootcamp.movie2gether.payment.dto.PaymentResponse;
import com.bootcamp.movie2gether.payment.entity.Payment;
import com.bootcamp.movie2gether.payment.mapper.PaymentMapper;
import com.bootcamp.movie2gether.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @Autowired
    PaymentMapper paymentMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse createPayment(@RequestBody PaymentRequest paymentRequest) {
        Payment payment = paymentService.createPayment(paymentMapper.toEntity(paymentRequest));
        return paymentMapper.toResponse(payment);
    }

}
