package hu.kunb.paymentapi.core.exceptions;

import lombok.Getter;

@Getter
public enum ErrorCodes {
  INSUFFICIENT_BALANCE(1000), CONCURRENT_PROCESSING(2000);

  private final int code;

  ErrorCodes(int code){
    this.code = code;
  }

}
