package items.items.infrastructure.persistence.item;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataItemRepository extends JpaRepository<ItemEntity, UUID> {

  Optional<ItemEntity> findByIdAndDeletedFalse(UUID id);
}
