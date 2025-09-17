package br.com.fiap.hospital.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserHasActiveConsultationException extends RuntimeException {
    public UserHasActiveConsultationException(String message) {
        super(message);
    }

}
