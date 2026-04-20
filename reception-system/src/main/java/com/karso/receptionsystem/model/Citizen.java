// src/main/java/com/karso/receptionsystem/model/Citizen.java
package com.karso.receptionsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "citizens")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "password")
public class Citizen extends User {

    @NotNull
    @Pattern(regexp = "^[0-9]{11}$", message = "Personal code must be exactly 11 digits")
    @Column(name = "personal_code", nullable = false, unique = true)
    private String personalCode;

    @Column(name = "address")
    private String address;

    public Citizen(String firstName, String lastName, String email,
                   String password, String phone,
                   String personalCode, String address) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setPhone(phone);
        this.personalCode = personalCode;
        this.address = address;
    }
}