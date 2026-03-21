package items.items.domain.ports.out;

import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ItemRepository {

  SavedItemAuditData save(Item item);
  SavedItemAuditData save(Item item, int version);

  Optional<Item> findByIdAndDeletedFalse(ItemId id);

  record SavedItemAuditData(int version, LocalDateTime createdAt, LocalDateTime updatedAt) {}
}
