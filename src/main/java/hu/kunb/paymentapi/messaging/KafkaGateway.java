package hu.kunb.paymentapi.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kunb.paymentapi.core.exceptions.GeneralError;
import hu.kunb.paymentapi.core.gateways.NotificationGateway;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaGateway implements NotificationGateway {

  public static final String TOPIC = "payment";
  private final ObjectMapper mapper;
  private final KafkaProducer<String, String> producer;

  @Override
  @Async
  @Transactional
  public void sendNotification(PaymentRecord paymentRecord) {
     producer.send(new ProducerRecord<>(TOPIC, createMessage(paymentRecord)), (recordMetadata, e) -> {
       if (e != null) {
         handleKafkaError(paymentRecord, e);
       } else {
         log.info("SUCCESS");
       }
     });
  }

  private void handleKafkaError(PaymentRecord paymentRecord, Throwable th) {
    log.error("Error sending kafka message {}", paymentRecord, th);
  }

  private String createMessage(PaymentRecord paymentRecord) {
    try {
      return mapper.writeValueAsString(paymentRecord);
    } catch (JsonProcessingException e) {
      log.error("exception generating kafka messege", e);
      throw new GeneralError(e.getMessage());
    }
  }
}
