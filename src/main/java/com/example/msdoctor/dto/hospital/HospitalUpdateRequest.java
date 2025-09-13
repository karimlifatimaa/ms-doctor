package com.example.msdoctor.dto.hospital;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalUpdateRequest {
    @NotBlank(message = "Hospital name cannot be blank")
    private String name;

    @NotBlank(message = "License number cannot be blank")
    private String licenseNumber;

    @NotNull(message = "Address information cannot be null")
    private AddressDto address;

    @NotNull(message = "Contact information cannot be null")
    private ContactInfoDto contactInfo;
    private Boolean isActive;
}
