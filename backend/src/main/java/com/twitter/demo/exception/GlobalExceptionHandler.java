package com.twitter.demo.exception;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


/* Spring Security içinde olup da elle oluşturmama gerek olmayan exceptionlar bunlar
mesela ben UserNtFoundException yazmışım ama gerek yok zaten security de var bu
   BadCredentialsException
UsernameNotFoundException
AccessDeniedException
LockedException
DisabledException
AccountExpiredException
CredentialsExpiredException
 */


@RestControllerAdvice
public class GlobalExceptionHandler {


    //Tüm hatalara aynı şablonda/tip te ErrorResponse adında dönüş yapacağız
    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, HttpServletRequest request, int status) {
        ErrorResponse errorResponse = new ErrorResponse(
                message,
                status,
                request.getRequestURI(),
                java.time.LocalDateTime.now()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    // @Size @NotBlank gibi validation hataları için
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // validasyonlarda oluşan tüm hataları tek bir string içerisinde toplayalım
        String message = ex.getBindingResult().getAllErrors().stream().map( error ->
                error.getDefaultMessage()
                ).collect(Collectors.joining(", ")); // hataları virgülle ayırarak birleştiriyoruz
        return buildErrorResponse(message, request, 400);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), request, 401);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(ex.getMessage(), request, 404);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(e.getMessage(), request, 404);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e, HttpServletRequest request) {
        return buildErrorResponse("Kullanıcı adı veya şifre hatalı", request, 401);
    }
    @ExceptionHandler(com.twitter.demo.exception.BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e, HttpServletRequest request) {
        return buildErrorResponse(e.getMessage(), request, 400);
    }

}

/*

Controller
↓
Service
↓
Repository
↓
Exception oluşursa
↓
GlobalExceptionHandler
↓
Standard JSON response


----- method argument için.... ----

Request geldi
↓
@Valid çalıştı
↓
Validation fail oldu
↓
Spring MethodArgumentNotValidException fırlattı
↓
GlobalExceptionHandler yakaladı
↓
Field error listesi alındı
↓
Mesajlar birleştirildi
↓
Temiz JSON response döndü
 */