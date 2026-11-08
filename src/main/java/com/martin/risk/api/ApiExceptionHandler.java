package com.martin.risk.api;

import com.martin.risk.service.NotFoundException;
import com.martin.risk.service.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;


@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> invalidPayment(MethodArgumentNotValidException e) {
        FieldError field = e.getBindingResult().getFieldError();
        String reason = field == null ? "invalid payment" : field.getField() + ": " + field.getDefaultMessage();
        return ResponseEntity.badRequest().body(Map.of("error", "INVALID_PAYMENT", "reason", reason));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "INVALID_API_KEY"));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "PAYMENT_NOT_FOUND"));
    }
}
