package com.karso.receptionsystem.controller;

import com.karso.receptionsystem.dto.request.SpecialistRequest;
import com.karso.receptionsystem.dto.responce.SpecialistResponse;
import com.karso.receptionsystem.service.SpecialistService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/specialists")
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialistResponse create(@Valid @RequestBody SpecialistRequest request) {
        return specialistService.create(request);
    }

    @GetMapping
    public List<SpecialistResponse> findAll() {
        return specialistService.findAll();
    }

    @GetMapping("/active")
    public List<SpecialistResponse> findAllActive() {
        return specialistService.findAllActive();
    }

    @GetMapping("/department/{department}")
    public List<SpecialistResponse> findByDepartment(@PathVariable String department) {
        return specialistService.findByDepartment(department);
    }

    @GetMapping("/{id}")
    public SpecialistResponse findById(@PathVariable Long id) {
        return specialistService.findById(id);
    }

    @PutMapping("/{id}")
    public SpecialistResponse update(@PathVariable Long id, @Valid @RequestBody SpecialistRequest request) {
        return specialistService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        specialistService.delete(id);
    }
}