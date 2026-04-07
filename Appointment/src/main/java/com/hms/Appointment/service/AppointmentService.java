package com.hms.Appointment.service;

import com.hms.Appointment.dto.AppointmentDTO;
import com.hms.Appointment.dto.AppointmentDetails;
import com.hms.Appointment.exception.HmsException;

public interface AppointmentService {

    Long scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException;

    void cancelAppointment(Long appointmentId) throws HmsException;

    void completeAppointment(Long appointmentId);

    void rescheduleAppointment(Long appointmentId, String newDateTime);

    AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException;

    AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException;
}
