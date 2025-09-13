package com.example.msdoctor.service.hospital;

import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.hospital.HospitalCreateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.dto.hospital.HospitalUpdateRequest;
import com.example.msdoctor.exception.DuplicateResourceException;
import com.example.msdoctor.exception.ResourceNotFoundException;
import com.example.msdoctor.mapper.DoctorMapper;
import com.example.msdoctor.mapper.HospitalMapper;
import com.example.msdoctor.model.Doctor;
import com.example.msdoctor.model.Hospital;
import com.example.msdoctor.repository.DoctorRepository;
import com.example.msdoctor.repository.HospitalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;
    private final HospitalMapper hospitalMapper;
    private final DoctorMapper doctorMapper;


    public HospitalServiceImpl(HospitalRepository hospitalRepository, DoctorRepository doctorRepository, HospitalMapper hospitalMapper, DoctorMapper doctorMapper) {
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
        this.hospitalMapper = hospitalMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public HospitalResponse createHospital(HospitalCreateRequest request) {
        log.info("Creating a new hospital with license number: {}", request.getLicenseNumber());
        hospitalRepository.findByName(request.getName()).ifPresent(hospital -> {
            throw new DuplicateResourceException("Hospital with this name already exists: " + request.getName());
        });
        hospitalRepository.findByLicenseNumber(request.getLicenseNumber()).ifPresent(hospital -> {
            throw new DuplicateResourceException("Hospital with license number already exists: " + request.getLicenseNumber());
        });
        Hospital hospital = hospitalMapper.toEntity(request);
        Hospital savedHospital = hospitalRepository.save(hospital);
        log.info("Hospital created successfully with ID: {}", savedHospital.getId());
        return hospitalMapper.toResponse(savedHospital);
    }

    @Override
    public HospitalResponse updateHospital(UUID id, HospitalUpdateRequest request) {
        log.info("Updating hospital with ID: {}", id);
        Hospital existingHospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        if (request.getLicenseNumber() != null && !request.getLicenseNumber().equals(existingHospital.getLicenseNumber())) {
            hospitalRepository.findByLicenseNumber(request.getLicenseNumber()).ifPresent(hospital -> {
                throw new DuplicateResourceException("Hospital with license number already exists: " + request.getLicenseNumber());
            });
        }
        hospitalMapper.updateFromDto(request, existingHospital);
        Hospital updatedHospital = hospitalRepository.save(existingHospital);
        log.info("Hospital updated successfully with ID: {}", updatedHospital.getId());
        return hospitalMapper.toResponse(updatedHospital);
    }

    @Override
    public HospitalResponse getHospitalById(UUID id) {
        log.info("Fetching hospital with ID: {}", id);
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        return hospitalMapper.toResponse(hospital);
    }

    @Override
    public HospitalResponse getHospitalByLicenseNumber(String licenseNumber) {
        log.info("Fetching hospital with license number: {}", licenseNumber);
        Hospital hospital = hospitalRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with license number: " + licenseNumber));
        return hospitalMapper.toResponse(hospital);
    }

    @Override
    public List<HospitalResponse> getAllHospitals() {
        log.info("Fetching all hospitals");
        List<Hospital> hospitals = hospitalRepository.findAll();
        return hospitalMapper.toResponseList(hospitals);
    }

    @Override
    public void softDeleteHospital(UUID id) {

        log.info("Soft deleting hospital with ID: {}", id);
        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        hospital.setIsActive(false);
        hospitalRepository.save(hospital);
        log.info("Hospital soft deleted successfully with ID: {}", id);
    }

    @Override
    public void hardDeleteHospital(UUID id) {

        Hospital hospital = hospitalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + id));
        log.info("Hard deleting hospital with ID: {}", id);
        hospitalRepository.deleteById(id);
        log.info("Hospital hard deleted successfully with ID: {}", id);
    }

    @Override
    public List<DoctorResponse> getDoctorsByHospitalId(UUID hospitalId) {
        log.info("Fetching doctors for hospital with ID: {}", hospitalId);
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
        return hospital.getDoctors().stream()
                .map(doctorMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void addDoctorToHospital(UUID hospitalId, UUID doctorId) {

        log.info("Adding doctor with ID {} to hospital with ID {}", doctorId, hospitalId);
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        if (hospital.getDoctors().contains(doctor)) {
            log.warn("Doctor with ID {} is already assigned to hospital with ID {}", doctorId, hospitalId);
            return;
        }
        hospital.getDoctors().add(doctor);
        doctor.getHospitals().add(hospital);
        hospitalRepository.save(hospital);
        log.info("Doctor with ID {} added to hospital with ID {} successfully", doctorId, hospitalId);
    }

    @Override
    public void removeDoctorFromHospital(UUID hospitalId, UUID doctorId) {

        log.info("Removing doctor with ID {} from hospital with ID {}", doctorId, hospitalId);
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        if (!hospital.getDoctors().contains(doctor)) {
            log.warn("Doctor with ID {} is not assigned to hospital with ID {}", doctorId, hospitalId);
            return;
        }
        hospital.getDoctors().remove(doctor);
        doctor.getHospitals().remove(hospital);
        hospitalRepository.save(hospital);
        log.info("Doctor with ID {} removed from hospital with ID {} successfully", doctorId, hospitalId);
    }
}
