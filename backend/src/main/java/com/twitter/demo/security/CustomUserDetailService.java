package com.twitter.demo.security;


import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




// UserDetailsService i yazınca application properties deki username ve şifre devre dışı kalmış oldu
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        /*
        Bizim DB User entity’mizi
        Spring Security’nin anlayacağı UserDetails objesine çeviriyor.
         */

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