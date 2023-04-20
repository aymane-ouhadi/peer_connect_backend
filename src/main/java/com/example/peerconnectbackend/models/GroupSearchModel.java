package com.example.peerconnectbackend.models;

import com.example.peerconnectbackend.entities.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroupSearchModel {

    private Group group;

    private boolean isMember;

}
