package items.items.application.usecase;

import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Login;
import items.items.domain.model.LoginWithPassword;
import items.items.domain.model.PasswordHash;
import items.items.domain.model.PasswordPlain;
import items.items.domain.model.User;
import items.items.domain.ports.in.AddUserUseCase;
import items.items.domain.ports.out.UserRepositoryPort;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AddUserUseCaseImpl implements AddUserUseCase {

  private final UserRepositoryPort userRepositoryPort;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AddUserResponse addUser(AddUserCommand command) {
    Login login = new Login(command.username());
    if (userRepositoryPort.existsByLogin(login)) {
      throw ItemExceptionUtil.loginInUse();
    }
    PasswordPlain plainPassword = new PasswordPlain(command.password());
    PasswordHash passwordHash = new PasswordHash(passwordEncoder.encode(plainPassword.password()));
    User newUser = User.createNew(new LoginWithPassword(new Login(command.username()), passwordHash));
    LocalDateTime createdAt = userRepositoryPort.save(newUser);

    return new AddUserResponse(
        newUser.getId().value(),
        newUser.getLoginWithPassword().login().login(),
        createdAt
    );
  }

}