package com.example.msdoctor.mapper;

import com.example.msdoctor.dto.hospital.*;
import com.example.msdoctor.model.Address;
import com.example.msdoctor.model.ContactInfo;
import com.example.msdoctor.model.Hospital;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class HospitalMapper {

    public Hospital toEntity(HospitalCreateRequest request) {
        return Hospital.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .licenseNumber(request.getLicenseNumber())
                .address(toAddressEntity(request.getAddress()))
                .contactInfo(toContactInfoEntity(request.getContactInfo()))
                .isActive(true)
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    public HospitalResponse toResponse(Hospital entity) {
        return HospitalResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .licenseNumber(entity.getLicenseNumber())
                .address(toAddressDto(entity.getAddress()))
                .contactInfo(toContactInfoDto(entity.getContactInfo()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public Hospital updateFromDto(HospitalUpdateRequest dto, Hospital entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getLicenseNumber() != null) {
            entity.setLicenseNumber(dto.getLicenseNumber());
        }
        if (dto.getAddress() != null) {
            entity.setAddress(toAddressEntity(dto.getAddress()));
        }
        if (dto.getContactInfo() != null) {
            entity.setContactInfo(toContactInfoEntity(dto.getContactInfo()));
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        entity.setUpdatedAt(OffsetDateTime.now());
        return entity;
    }

    public List<HospitalResponse> toResponseList(List<Hospital> entities) {
        return entities.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<Hospital> toEntityList(List<HospitalCreateRequest> requests) {
        return requests.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
    public AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }
        return AddressDto.builder()
                .city(address.getCity())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .state(address.getState())
                .street(address.getStreet())
                .build();
    }

    public Address toAddressEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        return Address.builder()
                .city(addressDto.getCity())
                .country(addressDto.getCountry())
                .postalCode(addressDto.getPostalCode())
                .state(addressDto.getState())
                .street(addressDto.getStreet())
                .build();
    }

    public ContactInfoDto toContactInfoDto(ContactInfo contactInfo) {
        if (contactInfo == null) {
            return null;
        }
        return ContactInfoDto.builder()
                .email(contactInfo.getEmail())
                .phone(contactInfo.getPhone())
                .website(contactInfo.getWebsite())
                .build();
    }

    public ContactInfo toContactInfoEntity(ContactInfoDto contactInfoDto) {
        if (contactInfoDto == null) {
            return null;
        }
        return ContactInfo.builder()
                .email(contactInfoDto.getEmail())
                .phone(contactInfoDto.getPhone())
                .website(contactInfoDto.getWebsite())
                .build();
    }
    public Set<HospitalResponse> toHospitalResponseSet(Set<Hospital> hospitals) {
        if (hospitals == null) {
            return null;
        }
        return hospitals.stream()
                .map(this::toResponse)
                .collect(Collectors.toSet());
    }
}
