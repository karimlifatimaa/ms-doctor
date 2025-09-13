package com.example.msdoctor.dto.doctor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorUpdateRequest {

    @Size(max = 150)
    private String specialization;

    @Size(max = 50)
    private String licenseNumber;

    @Size(max = 100)
    private String academicDegree;

    private Boolean isActive;
}
