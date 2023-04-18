package com.example.peerconnectbackend.models;

import com.example.peerconnectbackend.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PostDetailsModel {

    private Post post;

    private Group group;

    private User user;

    private List<Comment> comments;

    private List<Like> likes;

}
