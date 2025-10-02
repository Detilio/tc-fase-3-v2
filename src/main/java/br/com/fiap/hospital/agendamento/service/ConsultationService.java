package br.com.fiap.hospital.agendamento.service;

import br.com.fiap.hospital.agendamento.dto.ConsultationRequest;
import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.entity.ConsultationEntity;
import br.com.fiap.hospital.agendamento.mapper.ConsultationMapper;
import br.com.fiap.hospital.agendamento.repository.ConsultationRepository;
import br.com.fiap.hospital.config.RabbitMQConfig;
import br.com.fiap.hospital.notificacao.dto.NotificationDto;
import br.com.fiap.hospital.notificacao.service.NotificationService;
import br.com.fiap.hospital.shared.Role;
import br.com.fiap.hospital.shared.exception.ResourceNotFoundException;
import br.com.fiap.hospital.shared.exception.ValidateConsultationException;
import br.com.fiap.hospital.user.entity.UserEntity;
import br.com.fiap.hospital.user.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultationService {
    private final ConsultationRepository repository;
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;

    public ConsultationService(ConsultationRepository repository, NotificationService notificationService, RabbitTemplate rabbitTemplate, UserRepository userRepository) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
        this.userRepository = userRepository;
    }

    /**
     * Apenas Enfermeiros (NURSE) e Médicos (DOCTOR) podem criar consultas.
     */
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR')")
    public ConsultationResponse criarConsulta(ConsultationRequest request) {

        validaPaciente(request.getPatientId());
        validaDoutor(request.getDoctorId());

        if (validaDataConsultaPaciente(request.getPatientId(), request.getDate())) {
            throw new ValidateConsultationException("Já existe uma consulta agendada para este paciente nesta mesma data e horário.");
        }

        if (validaDataConsultaDoutor(request.getDoctorId(), request.getDate())) {
            throw new ValidateConsultationException("Já existe consulta para este doutor nesta mesma data e horário.");
        }

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
    public List<ConsultationResponse> listaPaciente(@P("patientId") Long patientId) {
        return repository.findByPatientId(patientId)
                .stream()
                .map(ConsultationMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Médicos e Enfermeiros podem editar consultas.
     */
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR')")
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
    @PreAuthorize("hasAnyRole('NURSE', 'DOCTOR') or (hasRole('PATIENT') and #patientId == authentication.principal.id)")
    public List<ConsultationResponse> listaConsultasFuturasPorPaciente(@P("patientId") Long patientId) {
        LocalDateTime agora = LocalDateTime.now();

        return repository.findByPatientIdAndDateAfter(patientId, agora)
                .stream()
                .map(ConsultationMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Valida se o paciente existe
     */
    public void validaPaciente(Long patientId) {
        UserEntity patient = userRepository.findById(patientId)
                .filter(u -> u.getRoles().contains(Role.PATIENT))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente com ID " + patientId + " não encontrado."));
    }

    /**
     * Valida se o doutor existe
     */
    public void validaDoutor(Long doctorId) {
        UserEntity doctor = userRepository.findById(doctorId)
                .filter(u -> u.getRoles().contains(Role.DOCTOR))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doutor com ID " + doctorId + " não encontrado."));
    }

    /**
     * Valida se já existe consulta para a data especificada para o paciente
     */
    public boolean validaDataConsultaPaciente(Long patientId, LocalDateTime date) {
        return repository.existsByPatientIdAndDate(patientId, date);
    }

    /**
     * Valida se já existe consulta para a data especificada para o doutor
     */
    public boolean validaDataConsultaDoutor(Long doutorId, LocalDateTime date) {
        return repository.existsByDoctorIdAndDate(doutorId, date);
    }
}