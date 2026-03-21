package items.items.infrastructure.persistence.user;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserRepository extends JpaRepository<UserEntity, UUID> {

  boolean existsByLogin(String login);

  Optional<UserEntity> findByLogin(String login);

}
