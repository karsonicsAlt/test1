// src/main/java/com/karso/receptionsystem/controller/AuthController.java
package com.karso.receptionsystem.controller;


import com.karso.receptionsystem.dto.request.LoginRequest;
import com.karso.receptionsystem.dto.request.RegisterRequest;
import com.karso.receptionsystem.dto.responce.AuthResponse;
import com.karso.receptionsystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}