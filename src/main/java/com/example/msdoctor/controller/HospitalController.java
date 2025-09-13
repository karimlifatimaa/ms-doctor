package com.example.msdoctor.controller;

import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.hospital.HospitalCreateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.dto.hospital.HospitalUpdateRequest;
import com.example.msdoctor.service.hospital.HospitalService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospitals")
@Slf4j
public class HospitalController {
    private final HospitalService hospitalService;

    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }
    @PostMapping
    public ResponseEntity<HospitalResponse> createHospital(@Valid @RequestBody HospitalCreateRequest request) {
        log.info("REST request to create Hospital : {}", request);
        HospitalResponse response = hospitalService.createHospital(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> updateHospital(@PathVariable UUID id, @Valid @RequestBody HospitalUpdateRequest request) {
        log.info("REST request to update Hospital with ID: {}", id);
        HospitalResponse response = hospitalService.updateHospital(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> getHospitalById(@PathVariable UUID id) {
        log.info("REST request to get Hospital by ID: {}", id);
        HospitalResponse response = hospitalService.getHospitalById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<HospitalResponse>> getAllHospitals() {
        log.info("REST request to get all Hospitals");
        List<HospitalResponse> responses = hospitalService.getAllHospitals();
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteHospital(@PathVariable UUID id) {
        log.info("REST request to soft delete Hospital with ID: {}", id);
        hospitalService.softDeleteHospital(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/hard/{id}")
    public ResponseEntity<Void> hardDeleteHospital(@PathVariable UUID id) {
        log.info("REST request to hard delete Hospital with ID: {}", id);
        hospitalService.hardDeleteHospital(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{hospitalId}/doctors/{doctorId}")
    public ResponseEntity<Void> addDoctorToHospital(@PathVariable UUID hospitalId, @PathVariable UUID doctorId) {
        log.info("REST request to add doctor with ID {} to hospital with ID {}", doctorId, hospitalId);
        hospitalService.addDoctorToHospital(hospitalId, doctorId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{hospitalId}/doctors/{doctorId}")
    public ResponseEntity<Void> removeDoctorFromHospital(@PathVariable UUID hospitalId, @PathVariable UUID doctorId) {
        log.info("REST request to remove doctor with ID {} from hospital with ID {}", doctorId, hospitalId);
        hospitalService.removeDoctorFromHospital(hospitalId, doctorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{hospitalId}/doctors")
    public ResponseEntity<List<DoctorResponse>> getDoctorsByHospitalId(@PathVariable UUID hospitalId) {
        log.info("REST request to get doctors for hospital with ID: {}", hospitalId);
        List<DoctorResponse> responses = hospitalService.getDoctorsByHospitalId(hospitalId);
        return ResponseEntity.ok(responses);
    }
}
