package com.example.peerconnectbackend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditProfileModel {

    private String firstName;

    private String lastName;

    private String email;

    private String college;

    private String major;

}
