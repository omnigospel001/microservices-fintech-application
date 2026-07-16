package com.fintech.mapper;

import com.fintech.entity.User;
import com.fintech.repository.UserRepo;
import com.fintech.request.UserRequest;
import com.fintech.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder encoder;
    private final UserRepo userRepo;

    public User saveUser(UserRequest userRequest) {

        if (userRequest == null) {
            return null;
        }

        return User.builder()
                .firstName(userRequest.firstname())
                .lastName(userRequest.lastname())
                .email(userRequest.email())
                .password(encoder.encode(userRequest.password()))
                .accountNumber(generateAccountNumber())
                .build();
    }


    public UserResponse findById(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAccountNumber()
        );
    }

    public UserResponse findByAccountNumber(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAccountNumber()
        );
    }



    public long generateAccountNumber(){
        long accountNumber;
        do {
            accountNumber = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        } while (userRepo.findByAccountNumber(accountNumber).isPresent());
        return accountNumber;
    }
}
