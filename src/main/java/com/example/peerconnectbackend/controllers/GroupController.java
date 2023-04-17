package com.example.peerconnectbackend.controllers;


import com.example.peerconnectbackend.entities.Group;
import com.example.peerconnectbackend.entities.GroupUser;
import com.example.peerconnectbackend.entities.Rule;
import com.example.peerconnectbackend.entities.User;
import com.example.peerconnectbackend.enumerations.RequestState;
import com.example.peerconnectbackend.enumerations.Role;
import com.example.peerconnectbackend.models.CreateGroupModel;
import com.example.peerconnectbackend.repositories.GroupRepository;
import com.example.peerconnectbackend.repositories.GroupUserRepository;
import com.example.peerconnectbackend.repositories.RuleRepository;
import com.example.peerconnectbackend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final RuleRepository ruleRepository;

    private final GroupUserRepository groupUserRepository;


    public GroupController(UserRepository userRepository, GroupRepository groupRepository, RuleRepository ruleRepository, GroupUserRepository groupUserRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.ruleRepository = ruleRepository;
        this.groupUserRepository = groupUserRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(
            @RequestBody CreateGroupModel groupModel
    ){
        //Extracting the admin object from the id provided in the model
        User user = userRepository.findById(groupModel.getAdminId()).orElse(null);


        //Creating the rules
        List<String> rulesIds = new ArrayList<>();

        for(String ruleDescription: groupModel.getRulesDescription()){
            Rule rule = Rule.builder()
                .description(ruleDescription)
                .build();

            rulesIds.add(ruleRepository.save(rule).getId());
        }

        //Creating the group entity
        Group group = Group.builder()
                .name(groupModel.getName())
                .description(groupModel.getDescription())
                .rules(rulesIds)
                .build();

        group = groupRepository.save(group);

        //Initializing the user as the admin of the group
        GroupUser groupUser = GroupUser.builder()
                .userId(user.getId())
                .groupId(group.getId())
                .role(Role.ADMIN)
                .requestState(RequestState.ACCEPTED)
                .build();

        groupUserRepository.save(groupUser);

        return new ResponseEntity<>(
                "Group created successfully",
                HttpStatus.OK
        );
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(
            @RequestParam String userId,
            @RequestParam String groupId
    ){
        try{
            GroupUser groupUser = GroupUser.builder()
                    .userId(userId)
                    .groupId(groupId)
                    .role(Role.USER)
                    .requestState(RequestState.PENDING)
                    .build();

            groupUserRepository.save(groupUser);

            return new ResponseEntity<>(
                    "Joining request was sent successfully",
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

    @PutMapping("/accept")
    public ResponseEntity<String> accept(
            @RequestParam String userId,
            @RequestParam String groupId
    ){
        try{

            User user = userRepository.findById(userId).orElse(null);
            Group group = groupRepository.findById(groupId).orElse(null);

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, groupId).orElse(null);

            groupUser.setRequestState(RequestState.ACCEPTED);

            groupUserRepository.save(groupUser);

            return new ResponseEntity<>(
                    "User " + user.getFirstName() + " successfully joined the group " + group.getName(),
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

    @PutMapping("/refuse")
    public ResponseEntity<String> refuse(
            @RequestParam String userId,
            @RequestParam String groupId
    ){
        try{

            User user = userRepository.findById(userId).orElse(null);
            Group group = groupRepository.findById(groupId).orElse(null);

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, groupId).orElse(null);

            groupUserRepository.delete(groupUser);

            return new ResponseEntity<>(
                    "User " + user.getFirstName() + " was successfully removed from the group " + group.getName(),
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
