package br.com.fiap.hospital.agendamento.service;

import br.com.fiap.hospital.agendamento.dto.ConsultationRequest;
import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.entity.ConsultationEntity;
import br.com.fiap.hospital.agendamento.mapper.ConsultationMapper;
import br.com.fiap.hospital.agendamento.repository.ConsultationRepository;
import br.com.fiap.hospital.config.RabbitMQConfig;
import br.com.fiap.hospital.notificacao.dto.NotificationDto;
import br.com.fiap.hospital.notificacao.service.NotificationService;
import br.com.fiap.hospital.shared.ResourceNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {
    private final ConsultationRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public ConsultationService(ConsultationRepository repository, NotificationService notificationService, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Apenas Enfermeiros (NURSE) e Médicos (DOCTOR) podem criar consultas.
     */
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR')")
    public ConsultationResponse criarConsulta(ConsultationRequest request) {
        ConsultationEntity entity = ConsultationMapper.toEntity(request);
        ConsultationEntity saved = repository.save(entity);
        ConsultationResponse response = ConsultationMapper.toResponse(saved);

        NotificationDto notification = new NotificationDto(
                response.getPatientId(),
                response.getDoctorId(),
                response.getDate()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, notification);

        return response;
    }

    /**
     * Médicos e Enfermeiros podem ver a lista de qualquer paciente.
     * Pacientes (PATIENT) só podem ver a sua própria lista.
     */
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR') or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public List<ConsultationResponse> listaPaciente(Long patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(ConsultationMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Apenas Médicos (DOCTOR) podem editar consultas.
     */
    @PreAuthorize("hasRole('DOCTOR')")
    public ConsultationResponse editarConsulta(Long consultaId, ConsultationRequest request) {
        ConsultationEntity existingConsultation = repository.findById(consultaId)
                .orElseThrow(() -> new ResourceNotFoundException("Consulta não encontrada com o ID: " + consultaId));
        ConsultationMapper.updateEntityFromRequest(request, existingConsultation);
        ConsultationEntity updatedConsultation = repository.save(existingConsultation);
        ConsultationResponse response = ConsultationMapper.toResponse(updatedConsultation);
        NotificationDto notification = new NotificationDto(
                response.getPatientId(),
                response.getDoctorId(),
                response.getDate()
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, notification);
        return response;
    }

    /**
     * Retorna uma lista de consultas futuras para um paciente específico.
     * @param patientId O ID do paciente.
     * @return Uma lista de ConsultationResponse.
     */
    public List<ConsultationResponse> listaConsultasFuturasPorPaciente(Long patientId) {
        LocalDateTime agora = LocalDateTime.now();

        return repository.findByPatientIdAndDateAfter(patientId, agora)
                .stream()
                .map(ConsultationMapper::toResponse)
                .collect(Collectors.toList());
    }
}