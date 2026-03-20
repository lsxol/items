package items.items.domain.ports.out;

import items.items.domain.model.ItemId;
import items.items.domain.model.ItemPermission;
import items.items.domain.model.UserId;
import java.util.Optional;

public interface ItemPermissionRepository {

  ItemPermission save(ItemPermission itemPermission);

  Optional<ItemPermission> findByUserIdAndItemId(UserId userId, ItemId itemId);

}
