package com.example.peerconnectbackend.controllers;

import com.example.peerconnectbackend.entities.Attendee;
import com.example.peerconnectbackend.entities.Event;
import com.example.peerconnectbackend.entities.GroupUser;
import com.example.peerconnectbackend.enumerations.Role;
import com.example.peerconnectbackend.repositories.AttendeeRepository;
import com.example.peerconnectbackend.repositories.EventRepository;
import com.example.peerconnectbackend.repositories.GroupUserRepository;
import com.example.peerconnectbackend.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            GroupUser groupUser = groupUserRepository.findByUserId(event.getId()).orElse(null);

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

}
