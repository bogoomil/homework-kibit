package hu.kunb.paymentapi.messaging;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.kunb.paymentapi.TestAppender;
import hu.kunb.paymentapi.core.exceptions.GeneralError;
import hu.kunb.paymentapi.core.model.PaymentRecord;
import java.util.UUID;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;

class KafkaGatewayTest {

  public static final UUID CLIENT_ID = UUID.randomUUID();
  public static final UUID TRANSACTION_ID = UUID.randomUUID();
  public static final long AMOUNT = 1000L;
  public static final String PAYMENT_RECORD = "paymentRecord";
  @Mock
  ObjectMapper objectMapper;

  @Mock
  KafkaProducer<String, String> kafkaProducer;

  PaymentRecord paymentRecord = new PaymentRecord(CLIENT_ID, TRANSACTION_ID, AMOUNT);

  KafkaGateway gateway;

  private TestAppender testAppender;
  private final Logger logger = (Logger) LoggerFactory.getLogger(KafkaGateway.class);

  @BeforeEach
  void setUp() {
    openMocks(this);
    gateway = new KafkaGateway(objectMapper, kafkaProducer);
    testAppender = new TestAppender();
    testAppender.start();
    logger.addAppender(testAppender);
  }

  @Test
  void sendNotification_ok() throws JsonProcessingException {
    given(objectMapper.writeValueAsString(paymentRecord)).willReturn(PAYMENT_RECORD);
    gateway.sendNotification(paymentRecord);
    ArgumentCaptor<ProducerRecord<String, String>> argumentCaptor = ArgumentCaptor.forClass(ProducerRecord.class);
    ArgumentCaptor<Callback> callBackCaptor = ArgumentCaptor.forClass(Callback.class);
    verify(kafkaProducer).send(argumentCaptor.capture(), callBackCaptor.capture());
    Callback callback = callBackCaptor.getValue();
    callback.onCompletion(null, null);
    ProducerRecord<String, String> producerRecord = argumentCaptor.getValue();
    assertThat(producerRecord.value()).isEqualTo(PAYMENT_RECORD);
    assertTrue(testAppender.getLogEvents().stream()
        .anyMatch(event -> event.getFormattedMessage().contains("SUCCESS")));

  }
  @Test
  void sendNotification_kafka_exception() throws JsonProcessingException {
    given(objectMapper.writeValueAsString(paymentRecord)).willReturn(PAYMENT_RECORD);
    gateway.sendNotification(paymentRecord);
    ArgumentCaptor<ProducerRecord<String, String>> argumentCaptor = ArgumentCaptor.forClass(ProducerRecord.class);
    ArgumentCaptor<Callback> callBackCaptor = ArgumentCaptor.forClass(Callback.class);
    verify(kafkaProducer).send(argumentCaptor.capture(), callBackCaptor.capture());
    ProducerRecord<String, String> producerRecord = argumentCaptor.getValue();
    Callback callback = callBackCaptor.getValue();
    callback.onCompletion(null, new RuntimeException());
    assertTrue(testAppender.getLogEvents().stream()
        .anyMatch(event -> event.getFormattedMessage().contains("Error sending")));

    assertThat(producerRecord.value()).isEqualTo(PAYMENT_RECORD);
  }
  @Test
  void sendNotification_throws_general_error() throws JsonProcessingException {
    given(objectMapper.writeValueAsString(paymentRecord)).willThrow(JsonProcessingException.class);
    assertThrows(GeneralError.class, () -> gateway.sendNotification(paymentRecord));
  }
}