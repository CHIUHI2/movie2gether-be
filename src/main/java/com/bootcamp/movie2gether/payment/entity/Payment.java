package com.bootcamp.movie2gether.payment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private ObjectId id;
    private String cardNumber;
    private Boolean paymentSuccess;
    private String cardType;
    private String orderNumber;
    private String name;

    public Payment(String name,String cardNumber, Boolean paymentSuccess, String cardType, String orderNumber) {
        this.cardNumber = cardNumber;
        this.paymentSuccess = paymentSuccess;
        this.cardType = cardType;
        this.orderNumber = orderNumber;
        this.name = name;
    }
}
