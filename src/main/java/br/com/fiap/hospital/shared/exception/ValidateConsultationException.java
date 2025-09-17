package br.com.fiap.hospital.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ValidateConsultationException extends RuntimeException {
    public ValidateConsultationException(String message) {
        super(message);
    }
}
