package hu.kunb.paymentapi.messaging;

import hu.kunb.paymentapi.core.gateways.NotificationGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaGateway implements NotificationGateway {

  private final KafkaProducerService kafkaProducerService;

  private static final String TOPIC = "payment";

  @Override
  public void sendNotification(PaymentRecord paymentRecord) {
    kafkaProducerService.sendMessage(TOPIC, paymentRecord);
  }
}
