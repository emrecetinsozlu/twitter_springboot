package com.twitter.demo.security;


import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();

    }
}


/*
    1. DB’den user aranır
2. User bulunursa Spring Security UserDetails objesine çevrilir
3. Password DB’deki hash olarak verilir
4. Spring kendi PasswordEncoder ile şifreyi kontrol eder
 */