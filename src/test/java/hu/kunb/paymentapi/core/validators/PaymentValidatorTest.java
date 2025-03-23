package hu.kunb.paymentapi.core.validators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;

import hu.kunb.paymentapi.core.exceptions.ErrorCodes;
import hu.kunb.paymentapi.core.exceptions.PaymentCreateException;
import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class PaymentValidatorTest {

  public static final UUID CLIENT_ID = UUID.randomUUID();
  public static final UUID TRANSACTION_ID = UUID.randomUUID();
  public static final long AMOUNT = 1000L;
  @Mock
  PersistentGateway persistentGateway;

  PaymentValidator validator;
  private PaymentRecord paymentRecord;

  @BeforeEach
  void setUp() {
    openMocks(this);
    validator = new PaymentValidator(persistentGateway);
    paymentRecord = new PaymentRecord(CLIENT_ID, TRANSACTION_ID, AMOUNT);
  }

  @Test
  void validateRecord_no_exception_is_thrown() {
    given(persistentGateway.getBalanceForClient(any())).willReturn(1001);
    given(persistentGateway.findByTransactionId(paymentRecord.transactionId())).willReturn(Optional.empty());
    assertDoesNotThrow(() -> validator.validateRecord(paymentRecord));
  }

  @Test
  void validateRecord_no_exception_is_thrown2() {
    given(persistentGateway.getBalanceForClient(any())).willReturn(1000);
    given(persistentGateway.findByTransactionId(paymentRecord.transactionId())).willReturn(Optional.empty());
    assertDoesNotThrow(() -> validator.validateRecord(paymentRecord));
  }

  @Test
  void validateRecord_insufficient_balance_is_thrown() {
    given(persistentGateway.getBalanceForClient(any())).willReturn(999);
    given(persistentGateway.findByTransactionId(paymentRecord.transactionId())).willReturn(Optional.empty());
    PaymentCreateException ex = assertThrows(PaymentCreateException.class, () -> validator.validateRecord(paymentRecord));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCodes.INSUFFICIENT_BALANCE);
  }

  @Test
  void validateRecord_concurrent_processing_is_thrown() {
    given(persistentGateway.getBalanceForClient(any())).willReturn(1001);
    given(persistentGateway.findByTransactionId(paymentRecord.transactionId())).willReturn(Optional.of(new PaymentRecord(CLIENT_ID, TRANSACTION_ID, AMOUNT)));
    PaymentCreateException ex = assertThrows(PaymentCreateException.class, () -> validator.validateRecord(paymentRecord));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCodes.CONCURRENT_PROCESSING);
  }
}