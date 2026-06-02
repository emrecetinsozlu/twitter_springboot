package com.twitter.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;


    // süreç şöyle biz kullanıcı login olunca token üretip httponly olacak şekilde cookie üzerinden kullanıcıye token veriyoruz
    //daha sonra frontend bu tokenı elle değil otomatik browser a setoldu(çünkühttponly dedik) her istekte gönderecek
    /*
    1. Frontend /login isteği atar
                    axios.post(
              "http://localhost:3000/tweet",
              { content: "Merhaba" },
              { withCredentials: true } kritik yer burası işte
            );
    2. Backend response ile Set-Cookie gönderir
    3. Browser cookie’yi saklar
    4. Sonraki requestlerde browser cookie’yi otomatik gönderir
    5. Backend JwtAuthenticationFilter cookie’den token’ı okur
     */



    // HEADERDAN TOKEN OKUMASI
    /*
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            // Burada token doğrulama işlemi yapılacak
            // Token doğrulama başarılı ise SecurityContextHolder'a Authentication objesi set edilecek
            // Sonrasında filterChain.doFilter(request, response) ile diğer filtrelere geçilecek
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

     */




    // COOKIE DEN TOKEN OKUNMASI
    /*
    /login
    ↓
    backend access_token cookie set eder
    ↓
    sonraki request
    ↓
    browser cookie’yi otomatik gönderir
    ↓
    JwtAuthenticationFilter cookie’den token’ı okur
    ↓
    SecurityContext authenticated olur
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromCookie(request);

        if (token == null) {
            //zincirdeki bir sonraki filtreye devrediyor. Yani bu filtrenin yapacağı "token kontrolü" adımını es geçip, isteğin normal akışında devam etmesine izin veriyor.
            filterChain.doFilter(request, response);
            return;
        }

        String username = jwtService.extractUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    customUserDetailService.loadUserByUsername(username);
            // token valid mi süresi dolmuş mu jwtservice içerisindeki .isTokenValid de kontrol ediliyor
            // token expire olmadıysa ve token içerisindeki username ile db deki username eşleşiyorsa token validdir diyoruz
            if (jwtService.isTokenValid(token, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> "access_token".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
