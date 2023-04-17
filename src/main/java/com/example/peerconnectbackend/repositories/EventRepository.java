package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
