package br.com.fiap.hospital.historico.dto;

public record ConsultationInput(Long patientId, Long doctorId, String date, String observations) {}
