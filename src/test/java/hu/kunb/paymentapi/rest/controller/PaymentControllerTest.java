package hu.kunb.paymentapi.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.kunb.paymentapi.core.service.PaymentService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.openapitools.model.PaymentCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PaymentControllerTest {

  public static final long AMOUNT = 1000L;
  public static final UUID TRANSACTION_ID = UUID.randomUUID();
  public static final UUID CLIENT_ID = UUID.randomUUID();
  @Mock
  PaymentService service;

  PaymentController controller;

  @BeforeEach
  void setUp() {
    openMocks(this);
    controller = new PaymentController(service);
  }

  @Test
  void createPayment() {
    ResponseEntity<Void> response = controller.createPayment(new PaymentCreateRequest(CLIENT_ID, TRANSACTION_ID, AMOUNT));
    verify(service).processPayment(CLIENT_ID, TRANSACTION_ID, AMOUNT);
    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }
}