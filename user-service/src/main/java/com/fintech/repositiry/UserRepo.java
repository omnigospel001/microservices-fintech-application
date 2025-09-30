package com.fintech.repositiry;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.fintech.entity.User;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {

    Optional<User> findByAccountNumber(Long accountNumber);
}
