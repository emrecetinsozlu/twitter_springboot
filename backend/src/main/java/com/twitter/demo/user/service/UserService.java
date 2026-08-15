package com.twitter.demo.user.service;


import com.twitter.demo.user.dto.UserRegisterRequest;
import com.twitter.demo.user.dto.UserResponse;

public interface UserService {
    UserResponse register(UserRegisterRequest user);
    UserResponse getCurrentUser();
    void deleteCurrentUser();
}
