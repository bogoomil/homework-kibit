package hu.kunb.paymentapi.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.kunb.paymentapi.core.gateways.NotificationGateway;
import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.core.validators.PaymentValidator;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

class PaymentServiceTest {

  public static final long AMOUNT = 1L;
  public static final UUID TRANSACTION_ID = UUID.randomUUID();
  public static final UUID CLIENT_ID = UUID.randomUUID();
  @Mock
  PersistentGateway persistentGateway;

  @Mock
  NotificationGateway notificationGateway;

  @Mock
  PaymentValidator paymentValidator;

  PaymentService paymentService;

  @BeforeEach
  void setUp() {
    openMocks(this);
    paymentService = new PaymentService(persistentGateway, notificationGateway, paymentValidator);
  }

  @Test
  void processPayment() {
    paymentService.processPayment(CLIENT_ID, TRANSACTION_ID, AMOUNT);
    ArgumentCaptor<PaymentRecord> argumentCaptor = ArgumentCaptor.forClass(PaymentRecord.class);
    verify(paymentValidator).validateRecord(argumentCaptor.capture());
    PaymentRecord paymentRecord = argumentCaptor.getValue();
    assertThat(paymentRecord.amount()).isEqualTo(AMOUNT);
    assertThat(paymentRecord.clientId()).isEqualTo(CLIENT_ID);
    assertThat(paymentRecord.transactionId()).isEqualTo(TRANSACTION_ID);
    verify(persistentGateway).save(paymentRecord);
    verify(notificationGateway).sendNotification(paymentRecord);
  }
}