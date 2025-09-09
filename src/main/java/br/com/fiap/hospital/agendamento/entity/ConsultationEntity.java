package br.com.fiap.hospital.agendamento.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "consultations")
public class ConsultationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private String observations;
    private Long patientId;
    private Long doctorId;

    public ConsultationEntity() {
    }
}
