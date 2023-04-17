package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    public User findByEmail(String email);
}
