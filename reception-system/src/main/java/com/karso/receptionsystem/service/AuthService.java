// src/main/java/com/karso/receptionsystem/service/AuthService.java
package com.karso.receptionsystem.service;


import com.karso.receptionsystem.dto.request.LoginRequest;
import com.karso.receptionsystem.dto.request.RegisterRequest;
import com.karso.receptionsystem.dto.responce.AuthResponse;
import com.karso.receptionsystem.model.*;
import com.karso.receptionsystem.repo.*;
import com.karso.receptionsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CitizenRepository    citizenRepository;
    private final SpecialistRepository specialistRepository;
    private final AdminRepository      adminRepository;
    private final PasswordEncoder      passwordEncoder;
    private final JwtUtil              jwtUtil;

    /** Public registration — creates a Citizen account */
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (citizenRepository.existsByEmail(req.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        if (citizenRepository.existsByPersonalCode(req.getPersonalCode()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Personal code already in use");

        Citizen citizen = new Citizen();
        citizen.setFirstName(req.getFirstName());
        citizen.setLastName(req.getLastName());
        citizen.setEmail(req.getEmail());
        citizen.setPassword(passwordEncoder.encode(req.getPassword()));
        citizen.setPhone(req.getPhone());
        citizen.setPersonalCode(req.getPersonalCode());
        citizen.setAddress(req.getAddress());
        citizen.setActive(true);
        citizenRepository.save(citizen);

        String token = jwtUtil.generateToken(citizen.getEmail(), "CITIZEN");
        return new AuthResponse(token, "CITIZEN",
                citizen.getEmail(), citizen.getFirstName(), citizen.getLastName());
    }

    /** Login — checks all three user tables, returns role-specific token */
    public AuthResponse login(LoginRequest req) {
        // Check admins first (highest privilege)
        var adminOpt = adminRepository.findByEmail(req.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            verifyPassword(req.getPassword(), admin.getPassword());
            String role = admin.isSuperAdmin() ? "SUPER_ADMIN" : "ADMIN";
            String token = jwtUtil.generateToken(admin.getEmail(), role);
            return new AuthResponse(token, role,
                    admin.getEmail(), admin.getFirstName(), admin.getLastName());
        }

        // Check specialists
        var specOpt = specialistRepository.findByEmail(req.getEmail());
        if (specOpt.isPresent()) {
            Specialist spec = specOpt.get();
            verifyPassword(req.getPassword(), spec.getPassword());
            String token = jwtUtil.generateToken(spec.getEmail(), "SPECIALIST");
            return new AuthResponse(token, "SPECIALIST",
                    spec.getEmail(), spec.getFirstName(), spec.getLastName());
        }

        // Check citizens
        var citizenOpt = citizenRepository.findByEmail(req.getEmail());
        if (citizenOpt.isPresent()) {
            Citizen citizen = citizenOpt.get();
            verifyPassword(req.getPassword(), citizen.getPassword());
            String token = jwtUtil.generateToken(citizen.getEmail(), "CITIZEN");
            return new AuthResponse(token, "CITIZEN",
                    citizen.getEmail(), citizen.getFirstName(), citizen.getLastName());
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    private void verifyPassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }
}