package hu.kunb.paymentapi.rest.controller;

import jakarta.validation.Valid;
import java.util.UUID;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.Payment;
import org.openapitools.model.PaymentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController implements PaymentsApi {

  @Override
  @PostMapping
  public ResponseEntity<org.openapitools.model.PaymentResponse> createPayment(@Valid @RequestBody Payment payment) {
    return ResponseEntity.status(201).body(new PaymentResponse().transactionId(UUID.randomUUID()));
  }

}
