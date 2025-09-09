package br.com.fiap.hospital.notificacao.service;

import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.repository.ConsultationRepository;
import br.com.fiap.hospital.config.RabbitMQConfig;
import br.com.fiap.hospital.notificacao.dto.NotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    // Usar um Logger é a melhor prática para exibir mensagens no console
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Simula o envio de uma notificação quando uma consulta é criada/alterada.
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void processarNotificacaoDeAgendamento(NotificationDto notification) {
        log.info("--- 📧 MENSAGEM RECEBIDA DA FILA 📧 ---");
        log.info("Lembrete para o Paciente ID [{}]: Sua consulta com o Médico ID [{}] foi confirmada para [{}].",
                notification.patientId(),
                notification.doctorId(),
                notification.date()
        );
        log.info("---------------------------------------");
    }
}
