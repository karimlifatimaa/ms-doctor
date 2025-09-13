package com.example.msdoctor.repository;

import com.example.msdoctor.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    Optional<Doctor> findByCitizenId(UUID citizenId);
    Optional<Doctor> findByLicenseNumber(String licenseNumber);
}
