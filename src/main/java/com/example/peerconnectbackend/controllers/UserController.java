package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.Group;
import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.enumerations.RequestState;
import com.example.peerconnectbackend.models.EditProfileModel;
import com.example.peerconnectbackend.models.UserProfileModel;
import com.example.peerconnectbackend.repositories.GroupRepository;
import com.example.peerconnectbackend.repositories.GroupUserRepository;
import com.example.peerconnectbackend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final GroupUserRepository groupUserRepository;

    private final GroupRepository groupRepository;

    public UserController(UserRepository userRepository, GroupUserRepository groupUserRepository, GroupRepository groupRepository) {
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
        this.groupRepository = groupRepository;
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

    @PutMapping("/edit")
    public ResponseEntity<String> edit(
        @RequestParam String userId,
        @RequestBody EditProfileModel editProfileModel
    ){

        try{
            User user = userRepository.findById(userId).orElse(null);

            user.setFirstName(editProfileModel.getFirstName());
            user.setLastName(editProfileModel.getLastName());
            user.setEmail(editProfileModel.getEmail());
            user.setCollege(editProfileModel.getCollege());
            user.setMajor(editProfileModel.getMajor());

            userRepository.save(user);

            return new ResponseEntity<>(
                    "User edited successfully",
                    HttpStatus.OK
            );

        }
        catch(Exception e){
            return new ResponseEntity<>(
                    "Something went wrong, try again",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/details")
    public ResponseEntity<UserProfileModel> details(
        @RequestParam String userId
    ){

        try {

            User user = userRepository.findById(userId).orElse(null);

            List<Group> groups = groupUserRepository.findAllByUserIdAndRequestState(
                    userId,
                    RequestState.ACCEPTED
            )
                    .stream()
                    .map(
                            groupUser -> groupRepository.findById(groupUser.getGroupId()).orElse(null)
                    )
                    .toList();

            UserProfileModel userProfileModel = UserProfileModel.builder()
                    .user(user)
                    .groups(groups)
                    .build();


            return new ResponseEntity<>(
                    userProfileModel,
                    HttpStatus.OK
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }



    }

}
