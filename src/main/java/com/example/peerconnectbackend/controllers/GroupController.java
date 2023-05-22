package com.example.peerconnectbackend.controllers;


import com.example.peerconnectbackend.entities.*;
import com.example.peerconnectbackend.enumerations.RequestState;
import com.example.peerconnectbackend.enumerations.Role;
import com.example.peerconnectbackend.models.CreateGroupModel;
import com.example.peerconnectbackend.models.GroupDetailsModel;
import com.example.peerconnectbackend.models.GroupSearchModel;
import com.example.peerconnectbackend.repositories.*;
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

    private final EventRepository eventRepository;

    private final PostRepository postRepository;

    private final GroupUserRepository groupUserRepository;


    public GroupController(UserRepository userRepository, GroupRepository groupRepository, RuleRepository ruleRepository, EventRepository eventRepository, PostRepository postRepository, GroupUserRepository groupUserRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.ruleRepository = ruleRepository;
        this.eventRepository = eventRepository;
        this.postRepository = postRepository;
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
                .picture(groupModel.getPicture())
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
            //Joining the provided user to the provided group and setting the request to its default state (pending)
            GroupUser groupUser = GroupUser.builder()
                    .userId(userId)
                    .groupId(groupId)
                    .role(Role.USER)
                    .requestState(RequestState.PENDING)
                    .build();

            //Saving the entity to the database
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

            //Changing the requestState of the user who wants to join to ACCEPTED
            groupUser.setRequestState(RequestState.ACCEPTED);

            //Updating the changes
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

    //This route will work for both refusing to add a user and banning him from the group (win win :))
    @PutMapping("/ban")
    public ResponseEntity<String> refuse(
            @RequestParam String userId,
            @RequestParam String groupId
    ){
        try{

            User user = userRepository.findById(userId).orElse(null);
            Group group = groupRepository.findById(groupId).orElse(null);

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, groupId).orElse(null);

            //Deleting the groupUser entity from the database
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

    @GetMapping("/search")
    public ResponseEntity<List<GroupSearchModel>> search(
            @RequestParam String q,
            @RequestParam String userId
    ){
        try{

            //Retrieving the groups from the user query
            List<Group> groups = groupRepository.findByNameFuzzy(q);

            //Mapping the list from a list of Groups to GroupSearchModel for the frontend
            List<GroupSearchModel> groupSearchList = groups
                    .stream()
                    .map(
                            group -> GroupSearchModel.builder()
                                    .group(group)
                                    .isMember(
                                            groupUserRepository.findByUserIdAndGroupIdAndRequestState(
                                                    userId,
                                                    group.getId(),
                                                    RequestState.ACCEPTED
                                            ).orElse(null) != null
                                    )
                                    .build()
                    )
                    .toList();

            return new ResponseEntity<>(
                    groupSearchList,
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

    @GetMapping("/details")
    public ResponseEntity<GroupDetailsModel> details(
            @RequestParam String groupId,
            @RequestParam String userId
    ){
        try{

            Group group = groupRepository.findById(groupId).orElse(null);

            List<Event> events = eventRepository.findAllByGroupId(groupId);

            boolean isMember = groupUserRepository.findByUserIdAndGroupIdAndRequestState(
                    userId,
                    groupId,
                    RequestState.ACCEPTED
            ).isPresent();

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, groupId).orElse(null);

            RequestState requestState;

            if (groupUser == null) {
                requestState = null;
            }
            else{
                requestState = groupUser.getRequestState();
            }

            List<User> members = groupUserRepository.findAllByGroupIdAndRequestState(
                groupId,
                RequestState.ACCEPTED
            )
                    .stream()
                    .map(
                        e -> userRepository.findById(e.getUserId()).orElse(null)
            )
            .toList();;

            List<Post> posts = postRepository.findAllByGroupId(groupId);

            GroupDetailsModel groupDetailsModel = GroupDetailsModel.builder()
                    .group(group)
                    .isMember(isMember)
                    .requestState(requestState)
                    .events(events)
                    .members(members)
                    .posts(posts)
                    .build();

            return new ResponseEntity<>(
                    groupDetailsModel,
                    HttpStatus.OK
            );

        }
        catch (Exception e){

            System.out.println("e : " + e.getMessage());

            return new ResponseEntity<>(
                    new GroupDetailsModel(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}
