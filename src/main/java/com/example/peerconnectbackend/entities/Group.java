package com.example.peerconnectbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "group")
@Builder
public class Group {

    @Id
    private String id;

    private LocalDateTime _createdAt = LocalDateTime.now();

    private String name;

    private String description;

    private List<String> rules = new ArrayList<>();

}
