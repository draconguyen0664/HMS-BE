package com.hms.ProfileMS.dto;

import com.hms.ProfileMS.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDate dob;
    private String phone;
    private String address;
    private String aadharNo;
    private BloodGroup bloodGroup;

    public Patient toEntity() {
        return new Patient(this.id, this.name, this.email, this.dob, this.phone, this.address,
                this.aadharNo, this.bloodGroup);
    }
}
