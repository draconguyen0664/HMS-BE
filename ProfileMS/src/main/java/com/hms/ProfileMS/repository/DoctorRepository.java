package com.hms.ProfileMS.repository;

import com.hms.ProfileMS.dto.DoctorDropdown;
import com.hms.ProfileMS.entity.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByLicenseNo(String licenseNo);

//    @Query(value = """
//            SELECT d.id AS id, CONCAT(d.first_name, ' ', d.last_name) AS name
//            FROM doctor d
//            """, nativeQuery = true)
//    List<DoctorDropdown> findAllDoctorDropdowns();
}

