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
  public void save(PaymentRecord paymentRecord) {
    repository.save(mapPaymentRecord(paymentRecord));
  }

  @Override
  public Integer getBalanceForClient(UUID clientId) {
    return 1000;
  }

  private PaymentEntity mapPaymentRecord(PaymentRecord paymentRecord){
    PaymentEntity entity =  new PaymentEntity();
    entity.setTransactionId(paymentRecord.transactionId());
    entity.setClientId(paymentRecord.clientId());
    entity.setAmount(paymentRecord.amount());
    return entity;
  }
}
