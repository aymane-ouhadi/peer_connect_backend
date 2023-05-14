package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.repositories.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    AttendeeRepository attendeeRepository;
    CommentRepository commentRepository;
    EventRepository eventRepository;
    GroupRepository groupRepository;
    GroupUserRepository groupUserRepository;
    LikeRepository likeRepository;
    PostRepository postRepository;
    RuleRepository ruleRepository;
    UserRepository userRepository;

    public AdminController(
            AttendeeRepository attendeeRepository,
            CommentRepository commentRepository,
            EventRepository eventRepository,
            GroupRepository groupRepository,
            GroupUserRepository groupUserRepository,
            LikeRepository likeRepository,
            PostRepository postRepository,
            RuleRepository ruleRepository,
            UserRepository userRepository
    ) {
        this.attendeeRepository = attendeeRepository;
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.groupRepository = groupRepository;
        this.groupUserRepository = groupUserRepository;
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.ruleRepository = ruleRepository;
        this.userRepository = userRepository;
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clear(){

        try{
            attendeeRepository.deleteAll();
            commentRepository.deleteAll();
            eventRepository.deleteAll();
            groupUserRepository.deleteAll();
            groupRepository.deleteAll();
            likeRepository.deleteAll();
            postRepository.deleteAll();
            ruleRepository.deleteAll();
            userRepository.deleteAll();

            return new ResponseEntity<>(
                    "Database is cleared",
                    HttpStatus.OK
            );
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    "Something wrong happened during DB clearance",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }

}
