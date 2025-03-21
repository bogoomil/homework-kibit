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

  public void validateRecord(PaymentRecord paymentRecord){
    checkBalance(paymentRecord);
    checkIfProcessed(paymentRecord);
  }

  private void checkBalance(PaymentRecord paymentRecord) {
    if(persistentGateway.getBalanceForClient(paymentRecord.clientId()) < paymentRecord.amount()){
      throw new PaymentCreateException(ErrorCodes.INSUFFICIENT_BALANCE);
    }
  }

  private void checkIfProcessed(PaymentRecord paymentRecord) {
    if(persistentGateway.findByTransactionId(paymentRecord.transactionId()).isPresent()){
      throw new PaymentCreateException(ErrorCodes.CONCURRENT_PROCESSING);
    }
  }
}
