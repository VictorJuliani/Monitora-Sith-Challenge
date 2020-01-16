package com.monitoratec.victor.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
public class ExceptionHandlingController {

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Data integrity violation")  // 400
    @ExceptionHandler(ValidationException.class)
    public void variableValidationError() {
        // Nothing to do
    }

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Data integrity violation")  // 400
    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationError() {
        // Nothing to do
    }
}
