package com.example.peerconnectbackend.models;


import com.example.peerconnectbackend.entities.Rule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
public class CreateGroupModel {

    private String adminId;

    private String name;

    private String description;

    private String picture;

    private List<String> rulesDescription;

}
