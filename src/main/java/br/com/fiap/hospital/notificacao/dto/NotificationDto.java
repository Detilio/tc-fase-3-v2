package br.com.fiap.hospital.notificacao.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(Long patientId,
                              Long doctorId,
                              LocalDateTime date) {
}
