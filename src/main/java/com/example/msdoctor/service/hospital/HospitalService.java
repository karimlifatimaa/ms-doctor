package com.example.msdoctor.service.hospital;

import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.hospital.HospitalCreateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;
import com.example.msdoctor.dto.hospital.HospitalUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface HospitalService {
    HospitalResponse createHospital(HospitalCreateRequest request);
    HospitalResponse updateHospital(UUID id, HospitalUpdateRequest request);
    HospitalResponse getHospitalById(UUID id);
    HospitalResponse getHospitalByLicenseNumber(String licenseNumber);
    List<HospitalResponse> getAllHospitals();
    void softDeleteHospital(UUID id);
    void hardDeleteHospital(UUID id);
    List<DoctorResponse> getDoctorsByHospitalId(UUID hospitalId);
    void addDoctorToHospital(UUID hospitalId, UUID doctorId);
    void removeDoctorFromHospital(UUID hospitalId, UUID doctorId);
}
