package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.Attendee;
import com.example.peerconnectbackend.entities.Event;
import com.example.peerconnectbackend.entities.GroupUser;
import com.example.peerconnectbackend.entities.Post;
import com.example.peerconnectbackend.enumerations.Role;
import com.example.peerconnectbackend.repositories.AttendeeRepository;
import com.example.peerconnectbackend.repositories.EventRepository;
import com.example.peerconnectbackend.repositories.GroupUserRepository;
import com.example.peerconnectbackend.repositories.UserRepository;
import com.example.peerconnectbackend.utils.Functions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepository eventRepository;

    private final GroupUserRepository groupUserRepository;

    private final AttendeeRepository attendeeRepository;

    public EventController(EventRepository eventRepository, UserRepository userRepository, GroupUserRepository groupUserRepository, AttendeeRepository attendeeRepository) {
        this.eventRepository = eventRepository;
        this.groupUserRepository = groupUserRepository;
        this.attendeeRepository = attendeeRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(
        @RequestBody Event event
    ){
        try {
            GroupUser groupUser = groupUserRepository.findByUserId(event.getUserId()).orElse(null);

            if(groupUser.getRole() != Role.ADMIN){
                return new ResponseEntity<>(
                        "Only admins can create events",
                        HttpStatus.UNAUTHORIZED
                );
            }

            eventRepository.save(event);
            return new ResponseEntity<>(
                    "Event created successfully",
                    HttpStatus.OK
            );
        }
        catch(Exception e){
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/attend")
    public ResponseEntity<String> attend(
        @RequestParam String userId,
        @RequestParam String eventId
    ){
        try{

            Event event = eventRepository.findById(eventId).orElse(null);

            GroupUser groupUser = groupUserRepository.findByUserIdAndGroupId(userId, event.getGroupId()).orElse(null);

            if(groupUser == null){
                return new ResponseEntity<>(
                        "You can't attend an event in a group you're not in",
                        HttpStatus.OK
                );
            }

            Attendee alreadyGoing = attendeeRepository.findByUserIdAndEventId(userId, eventId).orElse(null);

            if(alreadyGoing != null){
                return new ResponseEntity<>(
                        "You're no longer in the attendee list",
                        HttpStatus.OK
                );
            }

            Attendee attendee = Attendee.builder()
                    .userId(userId)
                    .eventId(eventId)
                    .build();

            attendeeRepository.save(attendee);

            return new ResponseEntity<>(
                    "Right on man, you're going to the event!",
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

    @GetMapping("/recent")
    public ResponseEntity<List<Event>> recent(
            @RequestParam String userId
    ){
        try{

            List<Event> recentEvents = new ArrayList<>();

            //Getting the ids of the groups the user is in
            List<String> userGroupsIds = groupUserRepository
                    .findAllByUserId(userId)
                    .stream()
                    .map(GroupUser::getGroupId)
                    .toList();

            System.out.println("Groups Ids : " + userGroupsIds);

            //Getting recent posts in every group
            for(String groupId : userGroupsIds){

                LocalDateTime now = LocalDateTime.now();

                List<Event> recentEventsInGroup = eventRepository.findBy_publishedAtBetweenAndGroupId(
                        now.minusDays(2),
                        now,
                        groupId
                );

                recentEvents.addAll(recentEventsInGroup);
            }

            System.out.println("Recent events : " + recentEvents);

            return new ResponseEntity<>(
                    Functions.sortEventsByPublishedDate(recentEvents),
                    HttpStatus.OK
            );

        }
        catch(Exception e){
            return new ResponseEntity<>(
                    new ArrayList<>(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}
