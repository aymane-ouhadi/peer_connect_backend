package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LikeRepository extends MongoRepository<Like, String> {

    Optional<Like> findByUserIdAndPostId(String userId, String postId);

}
