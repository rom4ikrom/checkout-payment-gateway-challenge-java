package com.checkout.payment.gateway.presentation.exception;

import com.checkout.payment.gateway.domain.exception.NonFoundException;
import com.checkout.payment.gateway.domain.exception.PaymentNotFoundException;
import com.checkout.payment.gateway.domain.exception.UnprocessableException;
import com.checkout.payment.gateway.presentation.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler(NonFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(PaymentNotFoundException ex) {
    return responseEntityFor(ex, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException ex) {
    return responseEntityFor(ex, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UnprocessableException.class)
  public ResponseEntity<ErrorResponse> handleException(UnprocessableException ex) {
    return responseEntityFor(ex, HttpStatus.UNPROCESSABLE_CONTENT);
  }

  private ResponseEntity<ErrorResponse> responseEntityFor(RuntimeException ex, HttpStatusCode code) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), code);
  }

}
