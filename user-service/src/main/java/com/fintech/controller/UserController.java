package com.fintech.controller;

import com.fintech.entity.User;
import com.fintech.request.LoginRequest;
import com.fintech.request.UserRequest;
import com.fintech.response.LoginResponse;
import com.fintech.response.UserResponse;
import com.fintech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.ok().body(userService.saveUser(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<UserResponse> findByAccountNumber(@PathVariable Long accountNumber) {
        return ResponseEntity.ok().body(userService.findByAccountNumber(accountNumber));
    }

}
