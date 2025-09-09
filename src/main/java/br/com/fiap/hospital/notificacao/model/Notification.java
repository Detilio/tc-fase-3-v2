package br.com.fiap.hospital.notificacao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Notification {
    private String message;
    private Long patientId;
}
