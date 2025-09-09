package br.com.fiap.hospital.historico.controller;

import br.com.fiap.hospital.agendamento.dto.ConsultationRequest;
import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.service.ConsultationService;
import br.com.fiap.hospital.historico.dto.ConsultationInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ConsultationHistoryGraphqlController {

    private final ConsultationService consultationService;

    public ConsultationHistoryGraphqlController(ConsultationService consultationService) {
        this.consultationService = consultationService;
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR') or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public List<ConsultationResponse> consultationsByPatient(@Argument Long patientId) {
        return consultationService.listaPaciente(patientId);
    }

    @QueryMapping
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR') or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public List<ConsultationResponse> futureConsultationsByPatient(@Argument Long patientId) {
        return consultationService.listaConsultasFuturasPorPaciente(patientId);
    }

    @MutationMapping
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR')")
    public ConsultationResponse createConsultation(@Argument ConsultationInput graphQLInput) {
        ConsultationRequest serviceRequest = new ConsultationRequest(
                graphQLInput.patientId(),
                graphQLInput.doctorId(),
                LocalDateTime.parse(graphQLInput.date()),
                graphQLInput.observations()
        );
        return consultationService.criarConsulta(serviceRequest);
    }

    @MutationMapping
    @PreAuthorize("hasRole('DOCTOR')")
    public ConsultationResponse updateConsultation(@Argument Long id, @Argument ConsultationInput graphQLInput) {
        ConsultationRequest serviceRequest = new ConsultationRequest(
                graphQLInput.patientId(),
                graphQLInput.doctorId(),
                LocalDateTime.parse(graphQLInput.date()),
                graphQLInput.observations()
        );
        return consultationService.editarConsulta(id, serviceRequest);
    }
}