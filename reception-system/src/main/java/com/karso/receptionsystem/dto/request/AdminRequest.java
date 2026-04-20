package com.karso.receptionsystem.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AdminRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private boolean isSuperAdmin;
}