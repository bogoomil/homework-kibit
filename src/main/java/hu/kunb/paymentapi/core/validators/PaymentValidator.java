package hu.kunb.paymentapi.core.validators;

import hu.kunb.paymentapi.core.exceptions.ErrorCodes;
import hu.kunb.paymentapi.core.exceptions.PaymentCreateException;
import hu.kunb.paymentapi.core.gateways.PersistentGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentValidator {

  private final PersistentGateway persistentGateway;

  public void validateRecord(PaymentRecord record){
    checkBalance(record);
    checkIfProcessed(record);
  }

  private void checkBalance(PaymentRecord record) {
    if(persistentGateway.getBalanceForClient(record.clientId()) < record.amount()){
      throw new PaymentCreateException(ErrorCodes.INSUFFICIENT_BALANCE);
    }
  }

  private void checkIfProcessed(PaymentRecord record) {
    if(persistentGateway.findByTransactionId(record.transactionId()).isPresent()){
      throw new PaymentCreateException(ErrorCodes.CONCURRENT_PROCESSING);
    }
  }
}
