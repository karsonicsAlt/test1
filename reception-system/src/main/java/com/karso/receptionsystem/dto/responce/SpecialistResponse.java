package com.karso.receptionsystem.dto.responce;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpecialistResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String jobTitle;
    private String department;
    private String description;
    private String officeNumber;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Integer appointmentDurationMinutes;
    private boolean isActive;
    private LocalDateTime createdAt;
}
