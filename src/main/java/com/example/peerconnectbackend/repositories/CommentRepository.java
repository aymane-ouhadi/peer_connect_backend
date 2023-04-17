package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
