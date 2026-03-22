package items.items.infrastructure.web;

import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import items.items.BaseIntegrationTest;
import items.items.config.security.AuthHelper;
import items.items.config.security.dto.AuthDto;
import items.items.domain.model.Login;
import items.items.domain.model.LoginWithPassword;
import items.items.domain.model.PasswordHash;
import items.items.domain.model.User;
import items.items.domain.ports.in.CreateItemUseCase.CreateItemCommand;
import items.items.domain.ports.out.UserRepositoryPort;
import items.items.infrastructure.web.ItemCommandController.EditItemRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public class ItemHistoryIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepositoryPort userRepositoryPort;

  @Test
  @WithMockUser(username = "test_user")
  @DisplayName("Powinien zarejestrować wszystkie rewizje notatki i zwrócić poprawną historię")
  public void shouldTrackItemHistoryViaEnvers() throws Exception {
    User user = User.createNew(new LoginWithPassword(new Login("login"), new PasswordHash("password_hash")));
    userRepositoryPort.save(user);
    AuthDto mockAuth = new AuthDto(user.getId().value(), "test_user");
    try (MockedStatic<AuthHelper> authMock = mockStatic(AuthHelper.class)) {
      authMock.when(AuthHelper::getAuth).thenReturn(mockAuth);
      String createJson = objectMapper.writeValueAsString(new CreateItemCommand("Wersja 1", "Treść 1"));

      String response = mockMvc.perform(post("/items")
              .contentType(MediaType.APPLICATION_JSON)
              .content(createJson))
          .andExpect(status().isCreated())
          .andReturn().getResponse().getContentAsString();

      UUID itemId = UUID.fromString(objectMapper.readTree(response).get("id").asText());

      editItem(itemId, "Wersja 2", "Treść 2", 0);
      editItem(itemId, "Wersja 3", "Treść 3", 1);

      mockMvc.perform(get("/items/{id}/history", itemId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.length()").value(3))
          .andExpect(jsonPath("$[0].title").value("Wersja 3"))
          .andExpect(jsonPath("$[2].title").value("Wersja 1"));
    }
  }

  private void editItem(UUID id, String title, String content, int version) throws Exception {
    String editJson = objectMapper.writeValueAsString(new EditItemRequest(title, content, version));
    mockMvc.perform(patch("/items/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(editJson))
        .andExpect(status().isOk());
  }
}
