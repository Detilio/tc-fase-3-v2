package br.com.fiap.hospital.agendamento.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ConsultationResponse {
    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDateTime date;
    private String observations;
}
