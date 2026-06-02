package com.twitter.demo.user.service;

import com.twitter.demo.security.CurrentUserService;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import com.twitter.demo.user.dto.UserMapper;
import com.twitter.demo.user.dto.UserRegisterRequest;
import com.twitter.demo.user.dto.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserService = currentUserService;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest request) {
        if(userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }
        if(userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // In a real application, make sure to hash the password before saving
                .build();
        User savedUser = userRepository.save(user);
        return UserMapper.toUserResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse getCurrentUser() {
        User currentUser = currentUserService.getCurrentUser();
        return UserMapper.toUserResponse(currentUser);
    }
}
