package com.bootcamp.movie2gether.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "session_seat", def = "{'sessionId':1, 'seatNumber':1}", unique = true)
public class Booking {
    @MongoId(FieldType.OBJECT_ID)
    private String id;
    private String userId;
    private String seatNumber;
    private String sessionId;
}
