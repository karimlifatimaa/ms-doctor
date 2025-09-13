package com.example.msdoctor.repository;

import com.example.msdoctor.model.Hospital;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HospitalRepository extends JpaRepository<Hospital, UUID> {
    Optional<Hospital> findByLicenseNumber(String licenseNumber);

    Optional<Object> findByName(@NotBlank(message = "Hospital name cannot be blank") String name);

}
