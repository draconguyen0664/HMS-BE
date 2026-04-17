package com.hms.Appointment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hms.Appointment.entity.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;
    private Long patientId;
    private Long doctorId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime appointmentTime;

    private Status status;
    private String reason;
    private String notes;

    public Appointment toEntity() {
        return new Appointment(
                id,
                patientId,
                doctorId,
                appointmentTime,
                status,
                reason,
                notes
        );
    }
}