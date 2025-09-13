package com.example.msdoctor.service.doctor;

import com.example.msdoctor.dto.doctor.DoctorCreateRequest;
import com.example.msdoctor.dto.doctor.DoctorResponse;
import com.example.msdoctor.dto.doctor.DoctorUpdateRequest;
import com.example.msdoctor.dto.hospital.HospitalResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorService {
    DoctorResponse createDoctor(DoctorCreateRequest request);
    DoctorResponse updateDoctor(UUID id, DoctorUpdateRequest request);
    DoctorResponse getDoctorById(UUID id);
    DoctorResponse getDoctorByCitizenId(UUID citizenId);
    DoctorResponse getDoctorByLicenseNumber(String licenseNumber);
    List<DoctorResponse> getAllDoctors();
    public List<HospitalResponse> getHospitalsByDoctorId(UUID doctorId);
    public void addHospitalToDoctor(UUID doctorId, UUID hospitalId);
    public void removeHospitalFromDoctor(UUID doctorId, UUID hospitalId);
    void softDeleteDoctor(UUID id);
    void hardDeleteDoctor(UUID id);
}
