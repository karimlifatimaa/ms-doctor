package com.example.msdoctor.dto.hospital;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @NotBlank(message = "Street cannot be blank")
    private String street;
    @NotBlank(message = "City cannot be blank")
    private String city;
    private String state;
    private String postalCode;
    @NotBlank(message = "Country cannot be blank")
    private String country;
}
