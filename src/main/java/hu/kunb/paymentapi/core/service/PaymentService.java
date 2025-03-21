package hu.kunb.paymentapi.core.service;

import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.core.validators.PaymentValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
  
  private final PersistentGateway persistentGateway;
  private final PaymentValidator validator;

  @Transactional
  public UUID processPayment(UUID clientId, UUID transactionId ,Long amount){
    log.info("processing payment, client: {}, transaction: {}, amount {}", clientId, transactionId, amount);
    PaymentRecord paymentRecord = new PaymentRecord(clientId, transactionId, amount);
    validator.validateRecord(paymentRecord);
    persistentGateway.save(paymentRecord);
    return paymentRecord.transactionId();
  }

}
