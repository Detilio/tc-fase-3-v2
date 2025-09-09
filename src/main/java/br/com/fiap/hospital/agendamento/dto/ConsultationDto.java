package br.com.fiap.hospital.agendamento.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ConsultationDto(Long id,
                              LocalDateTime date,
                              String observations,
                              Long patientId,
                              Long doctorId) {
}
