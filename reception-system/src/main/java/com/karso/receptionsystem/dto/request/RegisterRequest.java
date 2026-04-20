// src/main/java/com/karso/receptionsystem/dto/RegisterRequest.java
package com.karso.receptionsystem.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// Only citizens self-register; specialists/admins are created by admins
@Getter @Setter
public class RegisterRequest {
    @NotBlank private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String phone;
    @NotBlank private String personalCode;
    private String address;
}