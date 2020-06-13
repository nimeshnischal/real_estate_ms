package com.nimesh.real_estate_ms.exception.handler;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@NoArgsConstructor
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    private ResponseEntity handleException(RuntimeException ex, WebRequest webRequest) {
        logger.error("Caught RuntimeException: ", ex);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error_message", ex.getMessage());
        return this.handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @ExceptionHandler({ResponseStatusException.class})
    private ResponseEntity handleResponseStatusExceptions(ResponseStatusException ex, WebRequest webRequest) {
        logger.error("Caught ResponseStatusException: ", ex);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("error_message", ex.getReason());
        return this.handleExceptionInternal(ex, responseBody, new HttpHeaders(), ex.getStatus(), webRequest);
    }
}
