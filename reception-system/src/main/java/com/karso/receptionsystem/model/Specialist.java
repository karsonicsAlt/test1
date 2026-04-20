// src/main/java/com/karso/receptionsystem/model/Specialist.java
package com.karso.receptionsystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "specialists")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true, exclude = "password")
public class Specialist extends User {

    @NotNull
    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @NotNull
    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "office_number")
    private String officeNumber;

    @NotNull
    @Column(name = "work_start_time", nullable = false)
    private LocalTime workStartTime;

    @NotNull
    @Column(name = "work_end_time", nullable = false)
    private LocalTime workEndTime;

    @NotNull
    @Column(name = "appointment_duration_minutes", nullable = false)
    private Integer appointmentDurationMinutes = 30;

    public Specialist(String firstName, String lastName, String email,String password, String phone, String jobTitle,String department, String description, String officeNumber,
    LocalTime workStartTime, LocalTime workEndTime,
    Integer appointmentDurationMinutes) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setPhone(phone);
        this.jobTitle = jobTitle;
        this.department = department;
        this.description = description;
        this.officeNumber = officeNumber;
        this.workStartTime = workStartTime;
        this.workEndTime = workEndTime;
        this.appointmentDurationMinutes = appointmentDurationMinutes;
    }
}