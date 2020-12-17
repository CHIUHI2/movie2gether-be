package com.bootcamp.movie2gether.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(name = "session_seat", def = "{'sessionId':1, 'seatNumber':1}", unique = true)
public class Booking {
    @Id
    private ObjectId id;
    private ObjectId userId;
    private String seatNumber;
    private ObjectId sessionId;
    @CreatedDate
    private LocalDateTime createTime;
}
