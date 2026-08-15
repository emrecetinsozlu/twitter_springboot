package com.twitter.demo.config;

import com.twitter.demo.security.CustomUserDetailService;
import com.twitter.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailService customUserDetailService;

    // İsteklerin COntroller'a gitmeden önce ilk geçtiği süreç securityfilterchain.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.GET,"/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tweet/**").permitAll()
                        .requestMatchers("/api/auth/**","/users/register").permitAll()

                        .anyRequest().authenticated())
                //session ı stateless yapınca basic-auth a da gerek kalmadı
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Basic-auth işlemi header ile taşınır yani bir kullanıcı adı şifreyi postmande girdiğinde base64 ile encode edilip header üzerinden backende gönderilir
                //.httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    /*
    // YENİ WEB SECURITY CUSTOMIZER:
    // Bu ayar, belirtilen endpoint'leri Spring Security'nin TÜM filtre zincirinden muaf tutar.
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(HttpMethod.GET, "/tweet", "/tweet/**");
    }

    */

    /*
    Merhaba Backend.

Ben localhost:3200'den geliyorum.

POST isteği atabilir miyim?
Cookie gönderebilir miyim?
Authorization gönderebilir miyim?
     */

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3200","http://localhost:5173","http://192.168.1.5:5173","https://headband-sixtyfold-morbidly.ngrok-free.dev"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
        JWT veya Custom Filtre Yazıyorsanız: Patlama noktası burasıdır. Kendi filtrenizin içine girip private final AuthenticationManager authenticationManager; yazıp enjekte etmeye çalıştığınızda, Spring size şu hatayı verir:

"Ben uygulama içinde AuthenticationManager tipinde bir nesne (Bean) bulamadım, bunu nereye bağlayacağımı bilmiyorum."
     */


    /*
    CustomUserDetailsService = DB User -> UserDetails çevirici
    DaoAuthenticationProvider = UserDetails + PasswordEncoder ile doğrulayıcı
    AuthenticationManager = doğrulama sürecini başlatan yönetici
     */

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();

    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailService);

        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


}
