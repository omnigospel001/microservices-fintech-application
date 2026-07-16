package com.fintech.service;

import com.fintech.entity.User;
import com.fintech.mapper.Mapper;
import com.fintech.repository.UserRepo;
import com.fintech.request.LoginRequest;
import com.fintech.request.UserRequest;
import com.fintech.response.LoginResponse;
import com.fintech.response.UserResponse;
import com.fintech.security.JwtUtil;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    public User saveUser(UserRequest userRequest) {
       return userRepo.save(mapper.saveUser(userRequest));
    }

    public LoginResponse login(LoginRequest loginRequest) {
        User user = userRepo.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        log.info("Token: {}", token);

        return LoginResponse.builder().token(token).build();

    }

    public UserResponse findById(String id) {
       return userRepo.findByObjectId(id).map(mapper::findById)
                .orElseThrow(() -> new NotFoundException("No user with this ID: "+ id));
    }

    public UserResponse findByAccountNumber(Long accountNumber) {
       return userRepo.findByAccountNumber(accountNumber).map(mapper::findByAccountNumber)
                .orElseThrow(() -> new NotFoundException("No user with this Account Number"));
    }
}
