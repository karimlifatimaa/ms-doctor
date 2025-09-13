package com.example.msdoctor.dto.hospital;

import com.example.msdoctor.dto.doctor.DoctorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {
    private UUID id;
    private String name;
    private String licenseNumber;
    private AddressDto address;
    private ContactInfoDto contactInfo;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
