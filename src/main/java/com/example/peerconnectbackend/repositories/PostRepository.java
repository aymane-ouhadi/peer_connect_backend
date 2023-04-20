package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Post;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findBy_publishedAtBetweenAndGroupId(LocalDateTime start, LocalDateTime end, String groupId);

//    List<Post> findAllByGroupIdOrderBy_publishedAtDesc(String groupId);

    List<Post> findAllByGroupId(String groupId);
}
