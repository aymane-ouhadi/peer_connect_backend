package com.example.peerconnectbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "verification_token")
@Builder
public class VerificationToken {

    @Id
    private String id;

    private String token;

//    private LocalDateTime expiryDate;
//
//    private String userId;

}
