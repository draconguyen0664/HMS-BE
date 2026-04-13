package com.hms.ProfileMS.service;

import com.hms.ProfileMS.dto.DoctorDTO;
import com.hms.ProfileMS.dto.DoctorDropdown;
import com.hms.ProfileMS.dto.DoctorDropdownImpl;
import com.hms.ProfileMS.entity.Doctor;
import com.hms.ProfileMS.exception.HmsException;
import com.hms.ProfileMS.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException {
        if (doctorDTO.getEmail() != null &&
                doctorRepository.findByEmail(doctorDTO.getEmail()).isPresent())
            throw new HmsException("DOCTOR_ALREADY_EXISTS");

        if (doctorDTO.getLicenseNo() != null &&
                doctorRepository.findByLicenseNo(doctorDTO.getLicenseNo()).isPresent())
            throw new HmsException("DOCTOR_ALREADY_EXISTS");

        return doctorRepository.save(doctorDTO.toEntity()).getId();
    }

    @Override
    public DoctorDTO getDoctorById(Long id) throws HmsException {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new HmsException("DOCTOR_NOT_FOUND"))
                .toDTO();
    }

    @Override
    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws HmsException {
        try {
            Doctor existingDoctor = doctorRepository.findById(doctorDTO.getId())
                    .orElseThrow(() -> new HmsException("DOCTOR_NOT_FOUND"));

            existingDoctor.setDob(doctorDTO.getDob());
            existingDoctor.setPhone(doctorDTO.getPhone());
            existingDoctor.setAddress(doctorDTO.getAddress());
            existingDoctor.setLicenseNo(doctorDTO.getLicenseNo());
            existingDoctor.setSpecialization(doctorDTO.getSpecialization());
            existingDoctor.setDepartment(doctorDTO.getDepartment());
            existingDoctor.setTotalExp(doctorDTO.getTotalExp());

            return doctorRepository.save(existingDoctor).toDTO();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Boolean doctorExists(Long id) throws HmsException {
        return doctorRepository.existsById(id);
    }

    @Override
    public List<DoctorDropdown> getDoctorDropdowns() throws HmsException {
        List<DoctorDropdown> result = new ArrayList<>();

        Iterable<Doctor> doctors = doctorRepository.findAll();

        for (Doctor doctor : doctors) {
            String name = buildDoctorName(doctor);
            result.add(new DoctorDropdownImpl(doctor.getId(), name));
        }

        return result;
    }

    private String buildDoctorName(Doctor doctor) {
        String firstName = safe(getValue(doctor, "getFirstName"));
        String lastName = safe(getValue(doctor, "getLastName"));

        String fullName = (firstName + " " + lastName).trim();

        if (!fullName.isBlank()) {
            return fullName;
        }

        String name = safe(getValue(doctor, "getName"));
        if (!name.isBlank()) {
            return name;
        }

        String fullNameField = safe(getValue(doctor, "getFullName"));
        if (!fullNameField.isBlank()) {
            return fullNameField;
        }

        return "Doctor";
    }

    private Object getValue(Doctor doctor, String methodName) {
        try {
            return doctor.getClass().getMethod(methodName).invoke(doctor);
        } catch (Exception e) {
            return null;
        }
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString().trim();
    }
}