package com.twitter.demo.user.controller;


import com.twitter.demo.user.dto.UserRegisterRequest;
import com.twitter.demo.user.dto.UserResponse;
import com.twitter.demo.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me")
    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    @DeleteMapping("/me")
    public ResponseEntity<String> deleteCurrentUser() {
        userService.deleteCurrentUser();
        ResponseCookie cookie = ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Account deleted successfully");
    }


    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
