package com.fintech.service;

import com.fintech.entity.User;
import com.fintech.mapper.Mapper;
import com.fintech.repositiry.UserRepo;
import com.fintech.request.UserRequest;
import com.fintech.response.UserResponse;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final Mapper mapper;

    public User saveUser(UserRequest userRequest) {
       return userRepo.save(mapper.saveUser(userRequest));
    }

    public UserResponse findById(String id) {
       return userRepo.findById(id).map(mapper::findById)
                .orElseThrow(() -> new NotFoundException("No user with this ID"));
    }

    public UserResponse findByAccountNumber(Long accountNumber) {
       return userRepo.findByAccountNumber(accountNumber).map(mapper::findByAccountNumber)
                .orElseThrow(() -> new NotFoundException("No user with this Account Number"));
    }
}
