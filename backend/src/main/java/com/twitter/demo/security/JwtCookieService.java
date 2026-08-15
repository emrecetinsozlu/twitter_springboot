package com.twitter.demo.security;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;


// Bu service in amacı jwt tarafından üretilen token ı kullanıcıya httopnly olacak şekilde cookie olarak vermek
// bu işlemi AuthController login kısmında yapacağız
@Service
public class JwtCookieService {
    public void addJwtCookie(HttpServletResponse response, String token) {

        // Modern ve hatasız çerez oluşturma yöntemi
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(24 * 60 * 60) // 1 gün ömür
                .build();

        // Çerezi tek bir seferde ve en doğru şekilde HTTP Header'ına ekliyoruz
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
        /*
        // 1. Çerezi ResponseCookie ile modern şekilde oluşturun
        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)     // HTTPS (ngrok) için şart
                .sameSite("None") // İşte aradığımız metot!
                .path("/")
                .maxAge(3600)     // İsteğe bağlı: saniye cinsinden ömür
                .build();

// 2. Bu çerezi HTTP yanıtının (Response) Header kısmına ekleyin
// Not: "response" nesnesi HttpServletResponse nesnendir.
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /*
         */
}
