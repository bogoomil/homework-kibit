package hu.kunb.paymentapi.rest.controller;

import jakarta.validation.Valid;
import org.openapitools.api.PaymentsApi;
import org.openapitools.model.Payment;
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
  public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
    return ResponseEntity.status(201).body(payment);
  }

}
