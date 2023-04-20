package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findBy_publishedAtBetweenAndGroupId(LocalDateTime start, LocalDateTime end, String groupId);

//    List<Event> findAllByGroupIdOrderBy_publishedAtDesc(String groupId);

    List<Event> findAllByGroupId(String groupId);
}
