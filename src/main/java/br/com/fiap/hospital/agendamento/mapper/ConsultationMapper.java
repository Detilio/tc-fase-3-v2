// ConsultationMapper.java
package br.com.fiap.hospital.agendamento.mapper;

import br.com.fiap.hospital.agendamento.dto.ConsultationRequest;
import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.entity.ConsultationEntity;

public class ConsultationMapper {

    // Método para mapear de DTO de requisição para entidade
    public static ConsultationEntity toEntity(ConsultationRequest request) {
        return ConsultationEntity.builder()
                .date(request.getDate())
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .observations(request.getObservations())
                .build();
    }

    // Método para mapear de entidade para DTO de resposta
    public static ConsultationResponse toResponse(ConsultationEntity entity) {
        return ConsultationResponse.builder()
                .id(entity.getId())
                .patientId(entity.getPatientId())
                .doctorId(entity.getDoctorId())
                .date(entity.getDate())
                .observations(entity.getObservations())
                .build();
    }

    // Método para mapear os campos editáveis da consulta
    public static void updateEntityFromRequest(ConsultationRequest request, ConsultationEntity entity) {
        entity.setDoctorId(request.getDoctorId());
        entity.setPatientId(request.getPatientId());
        entity.setDate(request.getDate());
        entity.setObservations(request.getObservations());
    }
}