package items.items.application.usecase;

import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Login;
import items.items.domain.model.User;
import items.items.domain.ports.in.LoginUserUseCase;
import items.items.domain.ports.out.TokenProviderPort;
import items.items.domain.ports.out.TokenProviderPort.TokenInfo;
import items.items.domain.ports.out.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

  private final UserRepositoryPort userRepositoryPort;
  private final PasswordEncoder passwordEncoder;
  private final TokenProviderPort tokenProviderPort;

  @Override
  public LoginUserResponse loginUser(LoginUserCommand command) {
    Login login = new Login(command.username());
    User user = userRepositoryPort.findByLogin(login)
        .orElseThrow(ItemExceptionUtil::invalidCredentials);
    boolean isPasswordValid = passwordEncoder.matches(command.password(), user.getLoginWithPassword().passwordHash().value());
    if (!isPasswordValid) {
      throw ItemExceptionUtil.invalidCredentials();
    }
    TokenInfo tokenInfo = tokenProviderPort.generateToken(user);
    return new LoginUserResponse(tokenInfo.token(), tokenInfo.expiresInSeconds());
  }

}
