import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import hu.kunb.paymentapi.PaymentapiApplication;
import hu.kunb.paymentapi.persistence.repos.PaymentRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openapitools.model.PaymentCreateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = PaymentapiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
    locations = "classpath:application-test.yml")
class PaymentapiApplicationTests {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private PaymentRepository repository;

  @Test
  void contextLoads() {
  }

  @Test
  void intTest() throws Exception {
    PaymentCreateRequest request = new PaymentCreateRequest(UUID.randomUUID(), UUID.randomUUID(), 1000L);

    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
    String requestJson=ow.writeValueAsString(request);

    mvc.perform(post("/payments").contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk());
  }
}
