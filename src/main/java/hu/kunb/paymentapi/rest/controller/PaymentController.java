package hu.kunb.paymentapi.rest.controller;

import hu.kunb.paymentapi.core.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.PaymentCreateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController implements PaymentsApi {

  private final PaymentService service;

  @Override
  @PostMapping
  public ResponseEntity<Void> createPayment(@Valid @RequestBody PaymentCreateRequest request) {
    service.processPayment(request.getClientId(), request.getTransactionId(), request.getAmount());
    return ResponseEntity.ok().build();
  }
}
