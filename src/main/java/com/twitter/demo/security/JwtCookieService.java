package com.twitter.demo.security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;


// Bu service in amacı jwt tarafından üretilen token ı kullanıcıya httopnly olacak şekilde cookie olarak vermek
// bu işlemi AuthController login kısmında yapacağız
@Service
public class JwtCookieService {
    public void addJwtCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // prod'da true
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
