package com.example.peerconnectbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "user")
@Builder
public class User {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private boolean isConfirmed = false;

    private String college;

    private String major;

    private String password;

    private String profilePicture;

    private String coverPicture;

}
