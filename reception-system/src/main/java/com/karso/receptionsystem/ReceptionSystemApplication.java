package com.karso.receptionsystem;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.karso.receptionsystem.model.Admin;
import com.karso.receptionsystem.model.Citizen;
import com.karso.receptionsystem.model.Specialist;
import com.karso.receptionsystem.repo.AdminRepository;
import com.karso.receptionsystem.repo.CitizenRepository;
import com.karso.receptionsystem.repo.SpecialistRepository;

import java.time.LocalTime;

@SpringBootApplication
public class ReceptionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReceptionSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(
			CitizenRepository citizenRepo,
			AdminRepository adminRepo,
			SpecialistRepository specialistRepo,
			PasswordEncoder encoder
	) {
		return args -> {
			if (!citizenRepo.existsByPersonalCode("16118012345")) {
				Citizen citizen = new Citizen(
						"Janis",
						"Berzins",
						"janis@example.com",
						encoder.encode("password123"),
						"+37120000000",
						"16118012345",
						"Riga, Latvia"
				);
				citizenRepo.save(citizen);
			}

			if (!adminRepo.existsByEmail("admin@example.com")) {
				Admin admin = new Admin(
						"Admin",
						"User",
						"admin@example.com",
						encoder.encode("admin123"),
						"+37120000001",
						true
				);
				adminRepo.save(admin);
			}

			if (!specialistRepo.existsByEmail("specialist@example.com")) {
				Specialist specialist = new Specialist(
						"Dr. janis",
						"Berzins",
						"specialist@example.com",
						encoder.encode("specialist123"),
						"+37120000002",
						"Family Doctor",
						"General Medicine",
						"Experienced general practitioner",
						"Room 101",
						LocalTime.of(8, 0),
						LocalTime.of(17, 0),
						30
				);
				specialistRepo.save(specialist);
			}
		};
	}
}
