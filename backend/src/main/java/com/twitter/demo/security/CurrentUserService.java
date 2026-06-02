package com.twitter.demo.security;


import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

//Varsa eğer contextten kullanıcıyı çekeceğimiz bir service yazıyoruz
@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
