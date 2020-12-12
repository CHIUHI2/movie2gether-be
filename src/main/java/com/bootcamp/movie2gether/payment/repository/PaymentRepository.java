package com.bootcamp.movie2gether.payment.repository;

import com.bootcamp.movie2gether.payment.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {
}
