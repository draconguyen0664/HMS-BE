package com.hms.ProfileMS.service;

import com.hms.ProfileMS.dto.DoctorDTO;
import com.hms.ProfileMS.dto.DoctorDropdown;
import com.hms.ProfileMS.exception.HmsException;

import java.util.List;

public interface DoctorService {
    public Long addDoctor(DoctorDTO doctorDTO) throws HmsException;

    public DoctorDTO getDoctorById(Long id) throws HmsException;

    public DoctorDTO updateDoctor(DoctorDTO doctorDTO) throws HmsException;

    public Boolean doctorExists(Long id) throws HmsException;

    public List<DoctorDropdown> getDoctorDropdowns() throws HmsException;
}
