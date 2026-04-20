package com.karso.receptionsystem.service;

import com.karso.receptionsystem.dto.request.CitizenRequest;
import com.karso.receptionsystem.dto.request.CitizenProfileUpdateRequest;
import com.karso.receptionsystem.dto.responce.CitizenResponse;
import com.karso.receptionsystem.model.Citizen;
import com.karso.receptionsystem.repo.CitizenRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitizenService {

    private final CitizenRepository citizenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public CitizenResponse create(CitizenRequest request) {
        log.info("Creating citizen with email: {}", request.getEmail());

        if (citizenRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        if (citizenRepository.existsByPersonalCode(request.getPersonalCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Personal code already exists");
        }

        Citizen citizen = new Citizen();
        citizen.setFirstName(request.getFirstName());
        citizen.setLastName(request.getLastName());
        citizen.setEmail(request.getEmail());
        citizen.setPassword(passwordEncoder.encode(request.getPassword())); // ← hash password
        citizen.setPhone(request.getPhone());
        citizen.setPersonalCode(request.getPersonalCode());
        citizen.setAddress(request.getAddress());
        citizen.setActive(true); // ← new citizens are always active on creation

        Citizen saved = citizenRepository.save(citizen);
        log.info("Citizen created with ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CitizenResponse> findAll() {
        log.info("Fetching all citizens");
        return citizenRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CitizenResponse findById(Long id) {
        log.info("Fetching citizen with ID: {}", id);
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Citizen not found with ID: " + id));
        return toResponse(citizen);
    }

    @Transactional(readOnly = true)
    public CitizenResponse findByEmail(String email) {
        Citizen citizen = citizenRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Citizen not found with email: " + email));
        return toResponse(citizen);
    }

    @Transactional
    public CitizenResponse update(Long id, CitizenRequest request) {
        log.info("Updating citizen with ID: {}", id);
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Citizen not found with ID: " + id));

        // Check email conflict only if email is being changed
        if (!citizen.getEmail().equals(request.getEmail())
                && citizenRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        // Check personal code conflict only if it is being changed
        if (!citizen.getPersonalCode().equals(request.getPersonalCode())
                && citizenRepository.existsByPersonalCode(request.getPersonalCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Personal code already in use");
        }

        citizen.setFirstName(request.getFirstName());
        citizen.setLastName(request.getLastName());
        citizen.setEmail(request.getEmail());
        citizen.setPhone(request.getPhone());
        citizen.setPersonalCode(request.getPersonalCode());
        citizen.setAddress(request.getAddress());

        // Only re-hash password if a new one is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            citizen.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        log.info("Citizen updated with ID: {}", id);
        return toResponse(citizenRepository.save(citizen));
    }

    @Transactional
    public CitizenResponse updateByEmail(String email, CitizenProfileUpdateRequest request) {
        Citizen citizen = citizenRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Citizen not found with email: " + email));

        if (!citizen.getEmail().equals(request.getEmail())
                && citizenRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        if (!citizen.getPersonalCode().equals(request.getPersonalCode())
                && citizenRepository.existsByPersonalCode(request.getPersonalCode())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Personal code already in use");
        }

        citizen.setFirstName(request.getFirstName());
        citizen.setLastName(request.getLastName());
        citizen.setEmail(request.getEmail());
        citizen.setPhone(request.getPhone());
        citizen.setPersonalCode(request.getPersonalCode());
        citizen.setAddress(request.getAddress());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            citizen.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(citizenRepository.save(citizen));
    }

    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating citizen with ID: {}", id);
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Citizen not found with ID: " + id));
        citizen.setActive(false);
        citizenRepository.save(citizen);
        log.info("Citizen deactivated with ID: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting citizen with ID: {}", id);
        if (!citizenRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Citizen not found with ID: " + id);
        }
        citizenRepository.deleteById(id);
        log.info("Citizen deleted with ID: {}", id);
    }

    private CitizenResponse toResponse(Citizen citizen) {
        CitizenResponse response = new CitizenResponse();
        response.setId(citizen.getId());
        response.setFirstName(citizen.getFirstName());
        response.setLastName(citizen.getLastName());
        response.setEmail(citizen.getEmail());
        response.setPhone(citizen.getPhone());
        response.setPersonalCode(citizen.getPersonalCode());
        response.setAddress(citizen.getAddress());
        response.setIsActive(citizen.isActive());
        response.setCreatedAt(citizen.getCreatedAt());
        return response;
    }
}
