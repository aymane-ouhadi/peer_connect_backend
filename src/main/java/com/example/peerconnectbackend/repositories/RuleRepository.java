package com.example.peerconnectbackend.repositories;

import com.example.peerconnectbackend.entities.Rule;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RuleRepository extends MongoRepository<Rule, String> {
}
