package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Attendee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendeeRepository extends MongoRepository<Attendee, String> {

    Optional<Attendee> findByUserIdAndEventId(String userId, String eventId);

}
