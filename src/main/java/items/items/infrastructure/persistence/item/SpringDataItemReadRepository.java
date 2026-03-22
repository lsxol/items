package items.items.infrastructure.persistence.item;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataItemReadRepository extends JpaRepository<ItemEntity, UUID> {

  @Query("""
      SELECT DISTINCT i FROM ItemEntity i 
      LEFT JOIN i.permissions p 
      WHERE (i.ownerId = :userId OR p.userId = :userId)  
      AND i.deleted = false
      """)
  Page<ItemEntity> findAvailableItemsForUser(@Param("userId") UUID userId, Pageable pageable);
}
