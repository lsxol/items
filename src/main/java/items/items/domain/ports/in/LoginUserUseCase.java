package items.items.domain.ports.in;

import items.items.domain.exception.ItemExceptionUtil;

public interface LoginUserUseCase {

  LoginUserResponse loginUser(LoginUserCommand command);

  record LoginUserCommand(String username, String password) {
    public LoginUserCommand {
      if (username == null || password == null) {
        throw ItemExceptionUtil.userDataInvalid();
      }
    }
  }

  record LoginUserResponse(String token, int expiresIn) {

    public LoginUserResponse {
    }

  }
}
