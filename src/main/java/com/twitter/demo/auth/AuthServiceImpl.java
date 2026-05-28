package com.twitter.demo.auth;

import com.twitter.demo.exception.ResourceNotFoundException;
import com.twitter.demo.security.CustomUserDetailService;
import com.twitter.demo.security.JwtService;
import com.twitter.demo.user.User;
import com.twitter.demo.user.UserRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CustomUserDetailService customUserDetailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.customUserDetailService = customUserDetailService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResult login(LoginRequest loginRequest) {


        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        /*
        Spring framework :
        Kim authenticate oldu?
        → UserDetails bekleniyor herzaman oyüzden jwt token olarak userdetails tutuyoruz yoksa aslında user da tutabilridik.
         */

        UserDetails userDetails = customUserDetailService.loadUserByUsername(loginRequest.username());
        // Artık login olunca userdetails.username ile oluşturulmuş bir token kullanıcıya response'ta dönmüş olduk
        String token = jwtService.generateToken(userDetails);
        // token ı aynı zamanda cookie ile kullanıcı browser ına (httponly) vereceğiz
        return new AuthResult(user.getId(), user.getUsername(), token);



        /*
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new LoginResponse(user.getId(), user.getUsername(), "Login successful");
            */

    }


}
