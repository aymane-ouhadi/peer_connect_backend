package com.example.peerconnectbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentModel {

    private String userId;

    private String postId;

    private String comment;

}
