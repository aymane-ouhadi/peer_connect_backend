package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.GroupUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface GroupUserRepository extends MongoRepository<GroupUser, String> {

    Optional<GroupUser> findByUserId(String userId);

    Optional<GroupUser> findByUserIdAndGroupId(String userId, String groupId);
}
