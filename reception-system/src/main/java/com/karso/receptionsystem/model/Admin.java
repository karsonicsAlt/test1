// src/main/java/com/karso/receptionsystem/model/Admin.java
package com.karso.receptionsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "password")
public class Admin extends User {

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "is_super_admin", nullable = false)
    private boolean isSuperAdmin = false;

    public Admin(String firstName, String lastName,String email, String password,String phone, boolean isSuperAdmin) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setPhone(phone);
        this.isSuperAdmin = isSuperAdmin;
    }
}