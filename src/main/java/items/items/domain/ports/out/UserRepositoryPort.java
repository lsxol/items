package items.items.domain.ports.out;

import items.items.domain.model.Login;
import items.items.domain.model.User;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepositoryPort {

  LocalDateTime save(User user);

  Optional<User> findByLogin(Login login);

  boolean existsByLogin(Login login);

}
