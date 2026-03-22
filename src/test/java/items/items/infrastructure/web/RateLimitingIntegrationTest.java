package items.items.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import items.items.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
class RateLimitingIntegrationTest extends BaseIntegrationTest {

 @Autowired
 private MockMvc mockMvc;

 @Autowired
 private ObjectMapper objectMapper;

 @Test
 @DisplayName("Szóste żądanie logowania z tego samego IP powinno zwrócić 429 i nagłówek Retry-After")
 void shouldTriggerRateLimiterAfterFiveRequests() throws Exception {
  String loginJson = objectMapper.writeValueAsString(new AuthController.LoginRequest("user", "pass"));
  String clientIp = "192.168.1.100";

  for (int i = 0; i < 5; i++) {
   mockMvc.perform(post("/login")
       .with(request -> { request.setRemoteAddr(clientIp); return request; })
       .contentType(MediaType.APPLICATION_JSON)
       .content(loginJson));
  }
  mockMvc.perform(post("/login")
          .with(request -> { request.setRemoteAddr(clientIp); return request; })
          .contentType(MediaType.APPLICATION_JSON)
          .content(loginJson))
      .andExpect(status().isTooManyRequests())
      .andExpect(header().exists("Retry-After"));
 }
}