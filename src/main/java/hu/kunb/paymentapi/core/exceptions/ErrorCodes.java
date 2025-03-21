package hu.kunb.paymentapi.core.exceptions;

public enum ErrorCodes {
  INSUFFICIENT_BALANCE(1000), CONCURRENT_PROCESSING(2000);

  private int code;

  ErrorCodes(int code){
    this.code = code;
  }

  public int getCode(){
    return code;
  }
}
