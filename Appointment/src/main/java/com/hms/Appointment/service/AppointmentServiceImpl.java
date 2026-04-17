package com.hms.Appointment.service;

import com.hms.Appointment.client.ProfileClient;
import com.hms.Appointment.dto.AppointmentDTO;
import com.hms.Appointment.dto.AppointmentDetails;
import com.hms.Appointment.dto.DoctorDTO;
import com.hms.Appointment.dto.PatientDTO;
import com.hms.Appointment.dto.Status;
import com.hms.Appointment.entity.Appointment;
import com.hms.Appointment.exception.HmsException;
import com.hms.Appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ProfileClient profileClient;

    @Override
    public Long scheduleAppointment(AppointmentDTO appointmentDTO) throws HmsException {
        try {
            Boolean doctorExists = profileClient.doctorExists(appointmentDTO.getDoctorId());
            if (doctorExists == null || !doctorExists) {
                throw new HmsException("DOCTOR_NOT_FOUND");
            }

            Boolean patientExists = profileClient.patientExists(appointmentDTO.getPatientId());
            if (patientExists == null || !patientExists) {
                throw new HmsException("PATIENT_NOT_FOUND");
            }

            appointmentDTO.setStatus(Status.SCHEDULED);

            Appointment savedAppointment = appointmentRepository.save(appointmentDTO.toEntity());
            return savedAppointment.getId();

        } catch (HmsException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new HmsException("SOME_ERROR_OCCURRED");
        }
    }

    @Override
    public void cancelAppointment(Long appointmentId) throws HmsException {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HmsException("APPOINTMENT_NOT_FOUND"));

        if (appointment.getStatus().equals(Status.CANCELLED)) {
            throw new HmsException("APPOINTMENT_ALREADY_CANCELLED");
        }

        appointment.setStatus(Status.CANCELLED);
        appointmentRepository.save(appointment);
    }

    @Override
    public void completeAppointment(Long appointmentId) {
    }

    @Override
    public void rescheduleAppointment(Long appointmentId, String newDateTime) {
    }

    @Override
    public AppointmentDTO getAppointmentDetails(Long appointmentId) throws HmsException {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HmsException("APPOINTMENT_NOT_FOUND"))
                .toDTO();
    }

    @Override
    public AppointmentDetails getAppointmentDetailsWithName(Long appointmentId) throws HmsException {
        AppointmentDTO appointmentDTO = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new HmsException("APPOINTMENT_NOT_FOUND"))
                .toDTO();

        DoctorDTO doctorDTO = profileClient.getDoctorById(appointmentDTO.getDoctorId());
        PatientDTO patientDTO = profileClient.getPatientById(appointmentDTO.getPatientId());

        return new AppointmentDetails(
                appointmentDTO.getId(),
                appointmentDTO.getPatientId(),
                patientDTO.getName(),
                patientDTO.getEmail(),
                patientDTO.getPhone(),
                appointmentDTO.getDoctorId(),
                doctorDTO.getName(),
                appointmentDTO.getAppointmentTime(),
                appointmentDTO.getStatus(),
                appointmentDTO.getReason(),
                appointmentDTO.getNotes()
        );
    }
}