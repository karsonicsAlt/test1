// src/main/java/com/karso/receptionsystem/dto/AuthResponse.java
package com.karso.receptionsystem.dto.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String email;
    private String firstName;
    private String lastName;
}