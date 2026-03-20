package items.items.domain.ports.out;

import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepository {

  Item save(Item item);

  Optional<Item> findById(ItemId id);

}
