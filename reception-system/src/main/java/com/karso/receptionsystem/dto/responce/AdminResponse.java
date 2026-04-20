package com.karso.receptionsystem.dto.responce;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private boolean isSuperAdmin;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}
