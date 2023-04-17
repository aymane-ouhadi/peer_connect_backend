package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Attendee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AttendeeRepository extends MongoRepository<Attendee, String> {
}
