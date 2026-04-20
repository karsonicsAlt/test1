package com.karso.receptionsystem.service;

import com.karso.receptionsystem.dto.request.AdminRequest;
import com.karso.receptionsystem.dto.responce.AdminResponse;
import com.karso.receptionsystem.model.Admin;
import com.karso.receptionsystem.repo.AdminRepository;
import java.time.LocalDateTime;
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
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AdminResponse create(AdminRequest request) {
        log.info("Creating admin with email: {}", request.getEmail());

        if (adminRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        Admin admin = new Admin();
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setPhone(request.getPhone());
        admin.setSuperAdmin(request.isSuperAdmin());
        admin.setActive(true);

        Admin saved = adminRepository.save(admin);
        log.info("Admin created with ID: {}", saved.getId());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AdminResponse> findAll() {
        log.info("Fetching all admins");
        return adminRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminResponse findById(Long id) {
        log.info("Fetching admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + id));
        return toResponse(admin);
    }

    @Transactional
    public AdminResponse update(Long id, AdminRequest request) {
        log.info("Updating admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + id));

        // Check email conflict only if email is being changed
        if (!admin.getEmail().equals(request.getEmail())
                && adminRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setEmail(request.getEmail());
        admin.setPhone(request.getPhone());
        admin.setSuperAdmin(request.isSuperAdmin());

        // Only re-hash password if a new one is provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        log.info("Admin updated with ID: {}", id);
        return toResponse(adminRepository.save(admin));
    }

    // Called when admin successfully logs in
    @Transactional
    public void updateLastLogin(Long id) {
        log.info("Updating last login for admin ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + id));
        admin.setLastLogin(LocalDateTime.now());
        adminRepository.save(admin);
    }

    @Transactional
    public void deactivate(Long id) {
        log.info("Deactivating admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + id));

        if (admin.isSuperAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Super admin cannot be deactivated");
        }

        admin.setActive(false);
        adminRepository.save(admin);
        log.info("Admin deactivated with ID: {}", id);
    }

    @Transactional
    public void delete(Long id) {
        log.info("Deleting admin with ID: {}", id);
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + id));

        if (admin.isSuperAdmin()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Super admin cannot be deleted");
        }

        adminRepository.deleteById(id);
        log.info("Admin deleted with ID: {}", id);
    }

    private AdminResponse toResponse(Admin admin) {
        AdminResponse response = new AdminResponse();
        response.setId(admin.getId());
        response.setFirstName(admin.getFirstName());
        response.setLastName(admin.getLastName());
        response.setEmail(admin.getEmail());
        response.setPhone(admin.getPhone());
        response.setSuperAdmin(admin.isSuperAdmin());
        response.setActive(admin.isActive());
        response.setLastLogin(admin.getLastLogin());
        response.setCreatedAt(admin.getCreatedAt());
        return response;
    }
}