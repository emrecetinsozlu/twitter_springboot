package com.twitter.demo.user.dto;

import com.twitter.demo.user.User;

public class UserMapper {
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
