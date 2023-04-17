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
@Document(collection = "post")
@Builder
public class Post {

    @Id
    private String id;

    private LocalDateTime _publishedAt = LocalDateTime.now();

    private String userId;

    private String groupId;

    private String title;

    private String picture;

    private String description;

}
