package hu.kunb.paymentapi.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kunb.paymentapi.core.exceptions.ErrorCodes;
import hu.kunb.paymentapi.core.exceptions.PaymentCreateException;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String topic, PaymentRecord paymentRecord) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String message = mapper.writeValueAsString(paymentRecord);
      log.info("sending kafka message {}", message);
      kafkaTemplate.send(topic, message);
    } catch (Exception e) {
      log.error("error while sending message to kafka: ", e);
      throw new PaymentCreateException(ErrorCodes.KAFKA_EXCEPTION);
    }
  }
}