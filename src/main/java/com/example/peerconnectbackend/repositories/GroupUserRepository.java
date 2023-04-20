package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.GroupUser;
import com.example.peerconnectbackend.enumerations.RequestState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupUserRepository extends MongoRepository<GroupUser, String> {

    Optional<GroupUser> findByUserId(String userId);

    Optional<GroupUser> findByUserIdAndGroupId(String userId, String groupId);

    Optional<GroupUser> findByUserIdAndGroupIdAndRequestState(String userId, String groupId, RequestState state);

    List<GroupUser> findAllByUserId(String userId);
}
