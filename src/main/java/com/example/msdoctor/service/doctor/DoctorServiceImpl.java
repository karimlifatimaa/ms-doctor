package com.example.msdoctor.service.doctor;

import com.example.msdoctor.dto.doctor.DoctorCreateRequest;
import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.doctor.DoctorUpdateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.exception.DuplicateResourceException;
import com.example.msdoctor.exception.ResourceNotFoundException;
import com.example.msdoctor.mapper.DoctorMapper;
import com.example.msdoctor.mapper.HospitalMapper;
import com.example.msdoctor.model.Doctor;
import com.example.msdoctor.model.Hospital;
import com.example.msdoctor.repository.DoctorRepository;
import com.example.msdoctor.repository.HospitalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, DoctorMapper doctorMapper, HospitalRepository hospitalRepository, HospitalMapper hospitalMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
        this.hospitalRepository = hospitalRepository;
        this.hospitalMapper = hospitalMapper;
    }

    @Override
    public DoctorResponse createDoctor(DoctorCreateRequest request) {
        log.info("Creating a new doctor with citizen ID: {}", request.getCitizenId());
        doctorRepository.findByCitizenId(request.getCitizenId()).ifPresent(doctor -> {
            throw new DuplicateResourceException("Doctor with citizen ID already exists: " + request.getCitizenId());
        });
        doctorRepository.findByLicenseNumber(request.getLicenseNumber()).ifPresent(doctor -> {
            throw new DuplicateResourceException("Doctor with license number already exists: " + request.getLicenseNumber());
        });
        Doctor doctor = doctorMapper.toEntity(request);
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor created successfully with ID: {}", savedDoctor.getId());
        return doctorMapper.toResponse(savedDoctor);
    }

    @Override
    public DoctorResponse updateDoctor(UUID id, DoctorUpdateRequest request) {
        log.info("Updating doctor with ID: {}", id);
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        if (request.getLicenseNumber() != null && !request.getLicenseNumber().equals(existingDoctor.getLicenseNumber())) {
            doctorRepository.findByLicenseNumber(request.getLicenseNumber()).ifPresent(doctor -> {
                throw new DuplicateResourceException("Doctor with license number already exists: " + request.getLicenseNumber());
            });
        }
        doctorMapper.updateFromDto(request, existingDoctor);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        log.info("Doctor updated successfully with ID: {}", updatedDoctor.getId());
        return doctorMapper.toResponse(updatedDoctor);
    }

    @Override
    public DoctorResponse getDoctorById(UUID id) {
        log.info("Fetching doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public DoctorResponse getDoctorByCitizenId(UUID citizenId) {
        log.info("Fetching doctor with citizen ID: {}", citizenId);
        Doctor doctor = doctorRepository.findByCitizenId(citizenId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with citizen ID: " + citizenId));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public DoctorResponse getDoctorByLicenseNumber(String licenseNumber) {
        log.info("Fetching doctor with license number: {}", licenseNumber);
        Doctor doctor = doctorRepository.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with license number: " + licenseNumber));
        return doctorMapper.toResponse(doctor);
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        log.info("Fetching all doctors");
        List<Doctor> doctors = doctorRepository.findAll();
        return doctorMapper.toResponseList(doctors);
    }

    @Override
    public List<HospitalResponse> getHospitalsByDoctorId(UUID doctorId) {
        log.info("Fetching hospitals for doctor with ID: {}", doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        return doctor.getHospitals().stream()
                .map(hospitalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void addHospitalToDoctor(UUID doctorId, UUID hospitalId) {

        log.info("Adding hospital with ID {} to doctor with ID {}", hospitalId, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));

        if (doctor.getHospitals().contains(hospital)) {
            log.warn("Hospital with ID {} is already assigned to doctor with ID {}", hospitalId, doctorId);
            return;
        }

        doctor.getHospitals().add(hospital);
        Set<Hospital> hospitals = doctor.getHospitals();
        System.out.println(hospitals);
        hospital.getDoctors().add(doctor);
        doctorRepository.save(doctor);
        log.info("Hospital with ID {} added to doctor with ID {} successfully", hospitalId, doctorId);
    }

    @Override
    public void removeHospitalFromDoctor(UUID doctorId, UUID hospitalId) {

        log.info("Removing hospital with ID {} from doctor with ID {}", hospitalId, doctorId);
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospital not found with ID: " + hospitalId));

        if (!doctor.getHospitals().contains(hospital)) {
            log.warn("Hospital with ID {} is not assigned to doctor with ID {}", hospitalId, doctorId);
            return;
        }

        doctor.getHospitals().remove(hospital);
        hospital.getDoctors().remove(doctor);
        doctorRepository.save(doctor);
        log.info("Hospital with ID {} removed from doctor with ID {} successfully", hospitalId, doctorId);
    }

    @Override
    public void softDeleteDoctor(UUID id) {
        log.info("Soft deleting doctor with ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
        log.info("Doctor soft deleted successfully with ID: {}", id);
    }

    @Override
    public void hardDeleteDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + id));
        log.info("Hard deleting doctor with ID: {}", id);
        doctorRepository.deleteById(id);
        log.info("Doctor hard deleted successfully with ID: {}", id);
    }
}
