// src/main/java/com/karso/receptionsystem/dto/LoginRequest.java
package com.karso.receptionsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
}