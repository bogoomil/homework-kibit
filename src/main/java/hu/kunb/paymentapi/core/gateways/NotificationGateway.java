package hu.kunb.paymentapi.core.gateways;

import hu.kunb.paymentapi.core.model.PaymentRecord;

public interface NotificationGateway {
  void sendNotification(PaymentRecord paymentRecord);
}
