package com.example.peerconnectbackend.models;

import com.example.peerconnectbackend.entities.Event;
import com.example.peerconnectbackend.entities.Group;
import com.example.peerconnectbackend.entities.Post;
import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.enumerations.RequestState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroupDetailsModel {

    private Group group;

    private boolean isMember;

    private RequestState requestState;

    private List<User> members;

    private List<Event> events;

    private List<Post> posts;

}
