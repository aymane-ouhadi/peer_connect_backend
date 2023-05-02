package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.Group;
import com.example.peerconnectbackend.entities.GroupUser;
import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.enumerations.RequestState;
import com.example.peerconnectbackend.models.GroupSearchModel;
import com.example.peerconnectbackend.models.LoginUserModel;
import com.example.peerconnectbackend.models.SignUpUserModel;
import com.example.peerconnectbackend.models.UserProfileModel;
import com.example.peerconnectbackend.repositories.GroupRepository;
import com.example.peerconnectbackend.repositories.GroupUserRepository;
import com.example.peerconnectbackend.repositories.UserRepository;
import com.example.peerconnectbackend.utils.Functions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${spring.mail.username}")
    private String senderEmail;

    private final UserRepository userRepository;

    private final GroupUserRepository groupUserRepository;

    private final GroupRepository groupRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, GroupUserRepository groupUserRepository, GroupRepository groupRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.groupUserRepository = groupUserRepository;
        this.groupRepository = groupRepository;
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
                    "Welcome back, " +   user.getFirstName() + "!",
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

    @GetMapping("/user")
    public ResponseEntity<UserProfileModel> search(
            @RequestParam String userId
    ){
        try{
            //Retrieveing the user from the id
            User user = userRepository.findById(userId).orElse(null);

            //Get all groups of the user (GroupUser Collection) and mapping them to a list of group ids
            List<String> groupIds = groupUserRepository
                    .findAllByUserIdAndRequestState(userId, RequestState.ACCEPTED)
                    .stream().map(GroupUser::getGroupId).toList();

            //Mapping the group ids to the group objects
            //but taking only the first min(originalSize, 6)
            //to make my life easier in the frontend
            // (mobile application but I might work on a website later when I get more inspiration :3 )
            List<Group> groups = groupIds
                    .stream()
                    .map(groupId -> groupRepository.findById(groupId).orElse(null))
                    .toList()
                    .subList(0, Math.min(groupIds.size(), 6));

            //Building the profile model to send to the frontend
            UserProfileModel userProfileModel = UserProfileModel
                    .builder()
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
                    new UserProfileModel(),
                    HttpStatus.OK
            );
        }



    }

    @GetMapping("/groups")
    public ResponseEntity<List<GroupSearchModel>> groups(
            @RequestParam String userId
    ){
        try{

            //Get all groups of the user (GroupUser Collection) and mapping them to a list of group ids
            List<String> groupIds = groupUserRepository
                    .findAllByUserIdAndRequestState(userId, RequestState.ACCEPTED)
                    .stream().map(GroupUser::getGroupId).toList();

            //Mapping the group ids to the group objects
            List<Group> groups = groupIds
                    .stream()
                    .map(groupId -> groupRepository.findById(groupId).orElse(null))
                    .toList();

            //Building the profile model to send to the frontend
            List<GroupSearchModel> groupSearchModels = groups
                    .stream()
                    .map(group -> GroupSearchModel
                            .builder()
                            .group(group)
                            .isMember(false)
                            .build()
                    )
                    .toList();

            return new ResponseEntity<>(
                    groupSearchModels,
                    HttpStatus.OK
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    new ArrayList<>(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

}
