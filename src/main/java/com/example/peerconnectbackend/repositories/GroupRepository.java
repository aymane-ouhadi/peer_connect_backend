package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
}
