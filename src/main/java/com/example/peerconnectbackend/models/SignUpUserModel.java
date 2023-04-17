package com.example.peerconnectbackend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class SignUpUserModel {

    private String firstName;

    private String lastName;

    private String email;

    private String college;

    private String major;

    private String password;

    private String confirmPassword;

    private String profilePicture;

    private String coverPicture;

}
