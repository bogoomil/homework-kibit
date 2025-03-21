package hu.kunb.paymentapi.core.exceptions;

import lombok.Getter;

@Getter
public class PaymentCreateException extends RuntimeException{

  private final ErrorCodes errorCode;

  public PaymentCreateException(ErrorCodes errorCode) {
    super(errorCode.name());
    this.errorCode = errorCode;
  }
}
