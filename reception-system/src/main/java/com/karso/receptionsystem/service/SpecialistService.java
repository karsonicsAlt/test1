package com.karso.receptionsystem.service;

import com.karso.receptionsystem.dto.request.SpecialistRequest;
import com.karso.receptionsystem.dto.responce.SpecialistResponse;
import com.karso.receptionsystem.model.Specialist;
import com.karso.receptionsystem.repo.SpecialistRepository;
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
public class SpecialistService {

    private final SpecialistRepository specialistRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SpecialistResponse create(SpecialistRequest request) {
        log.info("Creating specialist with email: {}", request.getEmail());

        if (specialistRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Specialist specialist = new Specialist();
        specialist.setFirstName(request.getFirstName());
        specialist.setLastName(request.getLastName());
        specialist.setEmail(request.getEmail());
        specialist.setPassword(passwordEncoder.encode(request.getPassword()));
        specialist.setPhone(request.getPhone());
        specialist.setJobTitle(request.getJobTitle());
        specialist.setDepartment(request.getDepartment());
        specialist.setDescription(request.getDescription());
        specialist.setOfficeNumber(request.getOfficeNumber());
        specialist.setWorkStartTime(request.getWorkStartTime());
        specialist.setWorkEndTime(request.getWorkEndTime());
        specialist.setAppointmentDurationMinutes(
            request.getAppointmentDurationMinutes() != null
                ? request.getAppointmentDurationMinutes()
                : 30
        );
        specialist.setActive(true);

        Specialist saved = specialistRepository.save(specialist);
        log.info("Specialist created with ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SpecialistResponse> findAll() {
        log.info("Fetching all specialists");
        return specialistRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SpecialistResponse> findAllActive() {
        log.info("Fetching all active specialists");
        return specialistRepository.findByIsActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SpecialistResponse> findByDepartment(String department) {
        log.info("Fetching specialists in department: {}", department);
        return specialistRepository.findByDepartment(department)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SpecialistResponse findById(Long id) {
        log.info("Fetching specialist with ID: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Specialist not found with ID: " + id));
        return toResponse(specialist);
    }

    @Transactional
    public SpecialistResponse update(Long id, SpecialistRequest request) {
        log.info("Updating specialist with ID: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Specialist not found with ID: " + id));

        // Check email conflict only if email is being changed
        if (!specialist.getEmail().equals(request.getEmail())
                && specialistRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        specialist.setFirstName(request.getFirstName());
        specialist.setLastName(request.getLastName());
        specialist.setEmail(request.getEmail());
        specialist.setPhone(request.getPhone());
        specialist.setJobTitle(request.getJobTitle());
        specialist.setDepartment(request.getDepartment());
        specialist.setDescription(request.getDescription());
        specialist.setOfficeNumber(request.getOfficeNumber());
        specialist.setWorkStartTime(request.getWorkStartTime());
        specialist.setWorkEndTime(request.getWorkEndTime());

        if (request.getAppointmentDurationMinutes() != null) {
            specialist.setAppointmentDurationMinutes(request.getAppointmentDurationMinutes());
        }

        // Only re-hash password if a new one is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            specialist.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        log.info("Specialist updated with ID: {}", id);
        return toResponse(specialistRepository.save(specialist));
    }

    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating specialist with ID: {}", id);
        Specialist specialist = specialistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Specialist not found with ID: " + id));
        specialist.setActive(false);
        specialistRepository.save(specialist);
        log.info("Specialist deactivated with ID: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting specialist with ID: {}", id);
        if (!specialistRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Specialist not found with ID: " + id);
        }
        specialistRepository.deleteById(id);
        log.info("Specialist deleted with ID: {}", id);
    }

    private SpecialistResponse toResponse(Specialist specialist) {
        SpecialistResponse response = new SpecialistResponse();
        response.setId(specialist.getId());
        response.setFirstName(specialist.getFirstName());
        response.setLastName(specialist.getLastName());
        response.setEmail(specialist.getEmail());
        response.setPhone(specialist.getPhone());
        response.setJobTitle(specialist.getJobTitle());
        response.setDepartment(specialist.getDepartment());
        response.setDescription(specialist.getDescription());
        response.setOfficeNumber(specialist.getOfficeNumber());
        response.setWorkStartTime(specialist.getWorkStartTime());
        response.setWorkEndTime(specialist.getWorkEndTime());
        response.setAppointmentDurationMinutes(specialist.getAppointmentDurationMinutes());
        response.setActive(specialist.isActive());
        response.setCreatedAt(specialist.getCreatedAt());
        return response;
    }
}