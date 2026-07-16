package com.fintech.repository;

import com.fintech.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {

    @Query("{ '_id': ObjectId(?0) }")
    Optional<User> findByObjectId(String id);

    Optional<User> findByAccountNumber(Long accountNumber);

    Optional<User> findByEmail(String email);
}
