package com.twitter.demo.user.controller;


import com.twitter.demo.user.dto.UserRegisterRequest;
import com.twitter.demo.user.dto.UserResponse;
import com.twitter.demo.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest request){
        return userService.register(request);
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
