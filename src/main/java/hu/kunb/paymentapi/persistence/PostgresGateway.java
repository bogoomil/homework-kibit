package hu.kunb.paymentapi.persistence;

import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.persistence.entities.PaymentEntity;
import hu.kunb.paymentapi.persistence.repos.PaymentRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresGateway implements PersistentGateway {

  private final PaymentRepository repository;

  @Override
  public Optional<PaymentRecord> findByTransactionId(UUID transactionId) {
    return repository.findByTransactionId(transactionId).map(paymentEntity -> new PaymentRecord(paymentEntity.getClientId(), paymentEntity.getTransactionId(), paymentEntity.getAmount()));
  }

  @Override
  public void save(PaymentRecord record) {
    repository.save(mapPaymentRecord(record));
  }

  @Override
  public Integer getBalanceForClient(UUID clientId) {
    return 1000;
  }

  private PaymentEntity mapPaymentRecord(PaymentRecord record){
    PaymentEntity entity =  new PaymentEntity();
    entity.setTransactionId(record.transactionId());
    entity.setClientId(record.clientId());
    entity.setAmount(record.amount());
    return entity;
  }
}
