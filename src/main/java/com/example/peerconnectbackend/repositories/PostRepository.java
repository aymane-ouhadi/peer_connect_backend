package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
