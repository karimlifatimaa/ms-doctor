package com.example.msdoctor.dto.doctor;

import com.example.msdoctor.dto.hospital.HospitalResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {

    private UUID id;
    private UUID citizenId;
    private String specialization;
    private String licenseNumber;
    private String academicDegree;
    private Boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    private Set<UUID> hospitalIds;
}
