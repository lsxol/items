package items.items.infrastructure.persistence.user;

import items.items.domain.model.Login;
import items.items.domain.model.LoginWithPassword;
import items.items.domain.model.PasswordHash;
import items.items.domain.model.User;
import items.items.domain.model.UserId;
import items.items.domain.ports.out.UserRepositoryPort;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

  private final SpringDataUserRepository jpaUserRepository;

  @Override
  public LocalDateTime save(User user) {
    UserEntity entity = new UserEntity(
        user.getId().value(),
        user.getLoginWithPassword().login().login(),
        user.getLoginWithPassword().passwordHash().value(),
        LocalDateTime.now()
    );

    return jpaUserRepository.save(entity).getCreatedAt();
  }

  @Override
  public Optional<User> findByLogin(Login login) {
    return jpaUserRepository.findByLogin(login.login())
        .map(this::mapToDomain);
  }

  private User mapToDomain(UserEntity userEntity) {
    return new User(new UserId(userEntity.getId()),
        new LoginWithPassword(new Login(userEntity.getLogin()), new PasswordHash(userEntity.getPassword())));
  }

  @Override
  public boolean existsByLogin(Login login) {
    return jpaUserRepository.existsByLogin(login.login());
  }

}
