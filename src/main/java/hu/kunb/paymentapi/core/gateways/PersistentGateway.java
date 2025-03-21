package hu.kunb.paymentapi.core.gateways;

import hu.kunb.paymentapi.core.model.PaymentRecord;
import java.util.Optional;
import java.util.UUID;

public interface PersistentGateway {
  Optional<PaymentRecord> findByTransactionId(UUID transactionId);
  void save(PaymentRecord paymentRecord);
  Integer getBalanceForClient(UUID clientId);
}
