package com.example.peerconnectbackend.entities;

import com.example.peerconnectbackend.enumerations.RequestState;
import com.example.peerconnectbackend.enumerations.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor @AllArgsConstructor
@Document(collection = "group_user")
@Builder
public class GroupUser {

    @Id
    private String id;

    private String userId;

    private String groupId;

    private Role role;

    private RequestState requestState;

}
