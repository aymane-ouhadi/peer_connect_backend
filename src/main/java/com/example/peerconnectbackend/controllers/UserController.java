package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<User> user(
        @RequestParam String userId
    ){
        try{
            User user = userRepository.findById(userId).orElse(null);
            return new ResponseEntity<>(
                    user,
                    HttpStatus.OK
            );
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<User> findByEmail(
        @RequestParam String email
    ){
        try{

            System.out.println("Ha request jat");

            User user = userRepository.findByEmail(email);
            return new ResponseEntity<>(
                    user,
                    HttpStatus.OK
            );
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
