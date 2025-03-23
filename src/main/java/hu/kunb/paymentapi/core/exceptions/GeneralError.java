package hu.kunb.paymentapi.core.exceptions;

public class GeneralError extends RuntimeException{

  public GeneralError(String message) {
    super(message);
  }
}
