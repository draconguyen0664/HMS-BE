package com.hms.ProfileMS.service;

import com.hms.ProfileMS.dto.PatientDTO;
import com.hms.ProfileMS.entity.Patient;
import com.hms.ProfileMS.exception.HmsException;
import com.hms.ProfileMS.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Long addPatient(PatientDTO patientDTO) throws HmsException {
        if (patientDTO.getEmail() != null && patientRepository.findByEmail(patientDTO.getEmail()).isPresent())
            throw new HmsException("PATIENT_ALREADY_EXISTS");

        if (patientDTO.getAadharNo() != null &&
                patientRepository.findByAadharNo(patientDTO.getAadharNo()).isPresent())
            throw new HmsException("PATIENT_ALREADY_EXISTS");

        return patientRepository.save(patientDTO.toEntity()).getId();
    }


    @Override
    public PatientDTO getPatientById(Long id) throws HmsException {
        return patientRepository.findById(id).orElseThrow(() -> new HmsException("PATIENT_NOT_FOUND")).toDTO();
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) throws HmsException {
        try {
            Patient existingPatient = patientRepository.findById(patientDTO.getId())
                    .orElseThrow(() -> new HmsException("PATIENT_NOT_FOUND"));

            existingPatient.setDob(patientDTO.getDob());
            existingPatient.setPhone(patientDTO.getPhone());
            existingPatient.setAddress(patientDTO.getAddress());
            existingPatient.setAadharNo(patientDTO.getAadharNo());
            existingPatient.setBloodGroup(patientDTO.getBloodGroup());
            existingPatient.setAllergies(patientDTO.getAllergies());
            existingPatient.setChronicDisease(patientDTO.getChronicDisease());

            return patientRepository.save(existingPatient).toDTO();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Boolean patientExists(Long id) throws HmsException {
        return patientRepository.existsById(id);
    }
}
