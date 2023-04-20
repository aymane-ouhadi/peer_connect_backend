package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    List<Group> findByNameFuzzy(String name);

}
