package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.models.LoginUserModel;
import com.example.peerconnectbackend.models.SignUpUserModel;
import com.example.peerconnectbackend.repositories.UserRepository;
import com.example.peerconnectbackend.utils.Functions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody SignUpUserModel userModel
    ){

        try{
            //Checking if the email provided is an email
            if(!Functions.isEmail(userModel.getEmail())){
                return new ResponseEntity<>(
                        "Please provide a correct email format",
                        HttpStatus.UNPROCESSABLE_ENTITY
                );
            }

            //Checking if the password matches confirm password
            if(!userModel.getPassword().equals(userModel.getConfirmPassword())){
                return new ResponseEntity<>(
                        "The passwords you provided don't match",
                        HttpStatus.BAD_REQUEST
                );
            }

            //Checking if a user with the same email already exists
            User existingUser = userRepository.findByEmail(userModel.getEmail());
            if(existingUser != null){
                return new ResponseEntity<>(
                        "Email is already associated to another user",
                        HttpStatus.CONFLICT
                );
            }

            //Encoding the password
            String encodedPassword = passwordEncoder.encode(userModel.getPassword());

            User user = User.builder()
                    .firstName(userModel.getFirstName())
                    .lastName(userModel.getLastName())
                    .email(userModel.getEmail())
                    .college(userModel.getCollege())
                    .major(userModel.getMajor())
                    .password(encodedPassword)
                    .profilePicture(userModel.getProfilePicture())
                    .coverPicture(userModel.getCoverPicture())
                    .build();

            //Insert email verification process here

            //Saving the user
            userRepository.save(user);

            return new ResponseEntity<>(
                    "Welcome aboard, " + user.getFirstName() + "!",
                    HttpStatus.OK
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody LoginUserModel userModel
    ){

        try{

            User user = userRepository.findByEmail(userModel.getEmail());

            //If the provided email doesn't exist
            if(user == null){
                return new ResponseEntity<>(
                        "Please provide valid information",
                        HttpStatus.UNAUTHORIZED
                );
            }

            //If the provided password is correct
            if(!passwordEncoder.matches(userModel.getPassword(), user.getPassword())){
                return new ResponseEntity<>(
                        "Please provide valid information",
                        HttpStatus.UNAUTHORIZED
                );
            }


            return new ResponseEntity<>(
                    "Welcome back, " + user.getFirstName() + "!",
                    HttpStatus.OK
            );

        }
        catch (Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

}
