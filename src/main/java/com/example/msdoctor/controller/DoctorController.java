package com.example.msdoctor.controller;

import com.example.msdoctor.dto.doctor.DoctorCreateRequest;
import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.doctor.DoctorUpdateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.service.doctor.DoctorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/doctors")
@Slf4j
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(@Valid @RequestBody DoctorCreateRequest request) {
        log.info("REST request to create Doctor : {}", request);
        DoctorResponse response = doctorService.createDoctor(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@PathVariable UUID id, @Valid @RequestBody DoctorUpdateRequest request) {
        log.info("REST request to update Doctor with ID: {}", id);
        DoctorResponse response = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(@PathVariable UUID id) {
        log.info("REST request to get Doctor by ID: {}", id);
        DoctorResponse response = doctorService.getDoctorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DoctorResponse>> getAllDoctors() {
        log.info("REST request to get all Doctors");
        List<DoctorResponse> responses = doctorService.getAllDoctors();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteDoctor(@PathVariable UUID id) {
        log.info("REST request to soft delete Doctor with ID: {}", id);
        doctorService.softDeleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDeleteDoctor(@PathVariable UUID id) {
        log.info("REST request to hard delete Doctor with ID: {}", id);
        doctorService.hardDeleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{doctorId}/hospitals/{hospitalId}")
    public ResponseEntity<Void> addHospitalToDoctor(@PathVariable UUID doctorId, @PathVariable UUID hospitalId) {
        log.info("REST request to add hospital with ID {} to doctor with ID {}", hospitalId, doctorId);
        doctorService.addHospitalToDoctor(doctorId, hospitalId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{doctorId}/hospitals/{hospitalId}")
    public ResponseEntity<Void> removeHospitalFromDoctor(@PathVariable UUID doctorId, @PathVariable UUID hospitalId) {
        log.info("REST request to remove hospital with ID {} from doctor with ID {}", hospitalId, doctorId);
        doctorService.removeHospitalFromDoctor(doctorId, hospitalId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{doctorId}/hospitals")
    public ResponseEntity<List<HospitalResponse>> getHospitalsByDoctorId(@PathVariable UUID doctorId) {
        log.info("REST request to get hospitals for doctor with ID: {}", doctorId);
        List<HospitalResponse> responses = doctorService.getHospitalsByDoctorId(doctorId);
        return ResponseEntity.ok(responses);
    }
}
