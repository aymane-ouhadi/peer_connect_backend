package com.example.peerconnectbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "attendee")
@Builder
public class Attendee {

    @Id
    private String id;

    private String userId;

    private String eventId;

}
