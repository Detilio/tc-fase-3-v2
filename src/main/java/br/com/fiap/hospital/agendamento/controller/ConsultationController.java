package br.com.fiap.hospital.agendamento.controller;

import br.com.fiap.hospital.agendamento.dto.ConsultationRequest;
import br.com.fiap.hospital.agendamento.dto.ConsultationResponse;
import br.com.fiap.hospital.agendamento.service.ConsultationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultationController {
    private final ConsultationService service;

    public ConsultationController(ConsultationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ConsultationResponse> criar(@RequestBody ConsultationRequest dto) {
        return ResponseEntity.ok(service.criarConsulta(dto));
    }

    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<ConsultationResponse>> listarPorPaciente(@PathVariable Long id) {
        return ResponseEntity.ok(service.listaPaciente(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultationResponse> editar(
            @PathVariable Long id,
            @RequestBody ConsultationRequest dto) {
        ConsultationResponse response = service.editarConsulta(id, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paciente/{id}/futuras")
    public ResponseEntity<List<ConsultationResponse>> listarFuturasPorPaciente(@PathVariable("id") Long patientId) {
        List<ConsultationResponse> futurasConsultas = service.listaConsultasFuturasPorPaciente(patientId);
        return ResponseEntity.ok(futurasConsultas);
    }

}