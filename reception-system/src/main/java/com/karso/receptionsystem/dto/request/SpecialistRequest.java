package com.karso.receptionsystem.dto.request;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SpecialistRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private String jobTitle;
    private String department;
    private String description;
    private String officeNumber;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
    private Integer appointmentDurationMinutes;
}
