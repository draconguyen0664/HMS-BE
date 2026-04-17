package com.hms.Appointment.client;

import com.hms.Appointment.config.FeignClientInterceptor;
import com.hms.Appointment.dto.DoctorDTO;
import com.hms.Appointment.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "ProfileMS",
        url = "${profilems.url}",
        configuration = FeignClientInterceptor.class
)
public interface ProfileClient {

    @GetMapping("/profile/doctor/exists/{id}")
    Boolean doctorExists(@PathVariable("id") Long id);

    @GetMapping("/profile/patient/exists/{id}")
    Boolean patientExists(@PathVariable("id") Long id);

    @GetMapping("/profile/patient/get/{id}")
    PatientDTO getPatientById(@PathVariable("id") Long id);

    @GetMapping("/profile/doctor/get/{id}")
    DoctorDTO getDoctorById(@PathVariable("id") Long id);
}