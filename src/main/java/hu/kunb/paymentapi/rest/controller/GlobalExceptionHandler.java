package hu.kunb.paymentapi.rest.controller;


import hu.kunb.paymentapi.core.exceptions.PaymentCreateException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));

    return ResponseEntity.badRequest().body(new ErrorResponse().errorCode(400).message(errorMessage));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableExceptionException(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest().body(new ErrorResponse().errorCode(400).message(ex.getMessage()));
  }

  @ExceptionHandler(PaymentCreateException.class)
  public ResponseEntity<ErrorResponse> handlePaymentCreateException(PaymentCreateException ex) {
    return ResponseEntity.badRequest().body(new ErrorResponse().errorCode(ex.getErrorCode().getCode()).message(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> defaultHandler(Exception ex){
    log.error("default exception handler", ex);
    return ResponseEntity.badRequest().body(new ErrorResponse().errorCode(500).message(ex.getMessage()));
  }
}
