package com.example.msdoctor.dto.doctor;

import jakarta.validation.constraints.NotBlank;
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
public class DoctorCreateRequest {

    @NotNull(message = "Citizen ID cannot be null")
    private UUID citizenId;

    @NotBlank(message = "Specialization cannot be blank")
    @Size(max = 150)
    private String specialization;

    @NotBlank(message = "License number cannot be blank")
    @Size(max = 50)
    private String licenseNumber;

    private String academicDegree;
}