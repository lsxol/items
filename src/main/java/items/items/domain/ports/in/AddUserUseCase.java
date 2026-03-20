package items.items.domain.ports.in;

import items.items.domain.exception.ItemExceptionUtil;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AddUserUseCase {

  AddUserResponse addUser(AddUserCommand command);

  record AddUserCommand(String username, String password) {
    public AddUserCommand {
      if (username == null || password == null) {
        throw ItemExceptionUtil.userDataInvalid();
      }
    }
  }

  record AddUserResponse(UUID userId, String login, LocalDateTime createdAt) {

    public AddUserResponse {
    }

  }


}
