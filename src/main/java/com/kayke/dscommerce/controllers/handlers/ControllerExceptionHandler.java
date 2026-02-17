package com.kayke.dscommerce.controllers.handlers;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.kayke.dscommerce.dto.CustomError;
import com.kayke.dscommerce.dto.ValidationError;
import com.kayke.dscommerce.services.exceptions.DatabaseException;
import com.kayke.dscommerce.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

//! usado para interceptar globalmente minha aplicação
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> database(DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e,
            HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        ValidationError err = new ValidationError(Instant.now(), status.value(), "Dados inválidos",
                request.getRequestURI());

        List<FieldError> errors = e.getBindingResult().getFieldErrors();

        for (FieldError error : errors) {
            err.addError(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(status.value()).body(err);
    }
}
