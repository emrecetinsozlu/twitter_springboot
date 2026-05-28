package com.twitter.demo.auth;


import com.twitter.demo.security.JwtCookieService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtCookieService jwtCookieService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthResult result = authService.login(loginRequest);
        // token bilgisini cookie olarak browser a verelim
        jwtCookieService.addJwtCookie(response, result.token());
        return new LoginResponse(
                result.userId(),
                result.username(),
                "Login successful"
        );
    }
}
