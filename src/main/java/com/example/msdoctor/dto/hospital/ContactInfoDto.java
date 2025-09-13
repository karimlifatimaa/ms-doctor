package com.example.msdoctor.dto.hospital;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactInfoDto {
    @NotBlank(message = "Phone number cannot be blank")
    private String phone;
    @Email(message = "Invalid email format")
    private String email;
    private String website;
}
