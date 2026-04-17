package com.hms.Appointment.repository;

import com.hms.Appointment.entity.Appointment;
import org.springframework.data.repository.CrudRepository;

public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}