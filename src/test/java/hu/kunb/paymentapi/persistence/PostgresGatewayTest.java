package hu.kunb.paymentapi.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.persistence.entities.PaymentEntity;
import hu.kunb.paymentapi.persistence.repos.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

class PostgresGatewayTest {

  public static final UUID TRANSACTION_ID = UUID.randomUUID();
  public static final long AMOUNT = 1000L;
  public static final UUID CLIENT_ID = UUID.randomUUID();
  @Mock
  PaymentRepository repository;

  PostgresGateway gateway;

  @BeforeEach
  void setUp() {
    openMocks(this);
    gateway = new PostgresGateway(repository);
  }

  @Test
  void findByTransactionId() {
    PaymentEntity paymentEntity = getPaymentEntity();
    given(repository.findByTransactionId(TRANSACTION_ID)).willReturn(Optional.of(paymentEntity));
    Optional<PaymentRecord> paymentRecordOptional = gateway.findByTransactionId(TRANSACTION_ID);
    PaymentRecord paymentRecord = paymentRecordOptional.get();
    assertThat(paymentRecord.transactionId()).isEqualTo(TRANSACTION_ID);
  }

  private static PaymentEntity getPaymentEntity() {
    PaymentEntity paymentEntity = new PaymentEntity();
    paymentEntity.setTransactionId(TRANSACTION_ID);
    paymentEntity.setAmount(AMOUNT);
    paymentEntity.setClientId(CLIENT_ID);
    return paymentEntity;
  }

  @Test
  void save() {
    gateway.save(new PaymentRecord(CLIENT_ID, TRANSACTION_ID, AMOUNT));
    ArgumentCaptor<PaymentEntity> argumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);
    verify(repository).save(argumentCaptor.capture());
    PaymentEntity paymentEntity = argumentCaptor.getValue();
    assertThat(paymentEntity.getAmount()).isEqualTo(AMOUNT);
    assertThat(paymentEntity.getClientId()).isEqualTo(CLIENT_ID);
    assertThat(paymentEntity.getTransactionId()).isEqualTo(TRANSACTION_ID);
  }

  @Test
  void getBalanceForClient() {
    assertThat(gateway.getBalanceForClient(CLIENT_ID)).isEqualTo(1000);
  }
}