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
@Document(collection = "like")
@Builder
public class Like {

    @Id
    private String id;

    private String userId;

    private String postId;

}
