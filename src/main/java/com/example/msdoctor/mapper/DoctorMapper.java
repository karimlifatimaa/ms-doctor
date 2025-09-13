package com.example.msdoctor.mapper;

import com.example.msdoctor.dto.doctor.DoctorCreateRequest;
import com.example.msdoctor.dto.doctor.DoctorUpdateRequest;
import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.model.Doctor;
import com.example.msdoctor.model.Hospital;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DoctorMapper {


    public Doctor toEntity(DoctorCreateRequest request) {
        return Doctor.builder()
                .id(UUID.randomUUID())
                .citizenId(request.getCitizenId())
                .specialization(request.getSpecialization())
                .licenseNumber(request.getLicenseNumber())
                .academicDegree(request.getAcademicDegree())
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public DoctorResponse toResponse(Doctor entity) {
        return DoctorResponse.builder()
                .id(entity.getId())
                .citizenId(entity.getCitizenId())
                .specialization(entity.getSpecialization())
                .licenseNumber(entity.getLicenseNumber())
                .academicDegree(entity.getAcademicDegree())
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt().toInstant())
                .updatedAt(entity.getUpdatedAt().toInstant())
                .hospitalIds(entity.getHospitals() != null ?
                        entity.getHospitals().stream().map(Hospital::getId).collect(Collectors.toSet()) :
                        null)
                .build();
    }

    public Doctor updateFromDto(DoctorUpdateRequest dto, Doctor entity) {
        if (dto.getSpecialization() != null) {
            entity.setSpecialization(dto.getSpecialization());
        }
        if (dto.getLicenseNumber() != null) {
            entity.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getAcademicDegree() != null) {
            entity.setAcademicDegree(dto.getAcademicDegree());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    public List<DoctorResponse> toResponseList(List<Doctor> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }


}

