package hu.kunb.paymentapi.core.service;

import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import hu.kunb.paymentapi.core.validators.PaymentValidator;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
  
  private final PersistentGateway persistentGateway;
  private final PaymentValidator validator;
  
  public UUID processPayment(UUID clientId, UUID transactionId ,Long amount){
    PaymentRecord paymentRecord = new PaymentRecord(clientId, transactionId, amount);
    validator.validateRecord(paymentRecord);
    persistentGateway.save(paymentRecord);
    return paymentRecord.transactionId();
  }

}
