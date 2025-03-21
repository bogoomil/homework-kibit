package hu.kunb.paymentapi.core.service;

import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.core.validators.PaymentValidator;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  
  private final PersistentGateway persistentGateway;
  private final PaymentValidator validator;
  
  public UUID processPayment(UUID clientId, UUID transactionId ,Long amount){
    PaymentRecord record = new PaymentRecord(clientId, transactionId, amount);
    validator.validateRecord(record);
    persistentGateway.save(record);
    return record.transactionId();
  }

}
