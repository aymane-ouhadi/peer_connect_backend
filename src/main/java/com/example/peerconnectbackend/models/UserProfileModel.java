package com.example.peerconnectbackend.models;

import com.example.peerconnectbackend.entities.Group;
import com.example.peerconnectbackend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserProfileModel {

    private User user;

    private List<Group> groups;

}
