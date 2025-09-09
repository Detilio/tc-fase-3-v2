package br.com.fiap.hospital.historico.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class ConsultationHistory {
    private Long id;
    private LocalDateTime date;
    private String observations;
    private Long patientId;
    private Long doctorId;
}
