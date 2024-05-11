package com.wineko.api.controller.advices;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {EntityNotFoundException.class})
  protected ResponseEntity<Object> handleNotFound(
          RuntimeException ex, WebRequest request) {
      return handleExceptionInternal(
              ex, "Entity not found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(value = {HttpClientErrorException.class})
  protected ResponseEntity<Object> handleUnprocessableEntity(
          RuntimeException ex, WebRequest request) {
      return handleExceptionInternal(
              ex, "Unprocessable Entity", new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY, request);
  }
}
