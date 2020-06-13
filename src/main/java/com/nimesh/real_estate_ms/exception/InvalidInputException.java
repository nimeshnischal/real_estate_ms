package com.nimesh.real_estate_ms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidInputException extends ResponseStatusException {
    public InvalidInputException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public InvalidInputException(String reason) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
    }

    public InvalidInputException(String reason, Throwable cause) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, reason, cause);
    }
}
