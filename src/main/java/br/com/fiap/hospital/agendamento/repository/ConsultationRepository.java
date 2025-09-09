package br.com.fiap.hospital.agendamento.repository;

import br.com.fiap.hospital.agendamento.entity.ConsultationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<ConsultationEntity, Long> {
    List<ConsultationEntity> findByPatientId(Long patientId);

    Optional<ConsultationEntity> findById(Long id);

    List<ConsultationEntity> findByPatientIdAndDateAfter(Long patientId, LocalDateTime currentDate);
}
