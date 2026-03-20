package items.items.domain.ports.in;

import java.util.UUID;

public interface DeleteItemUseCase {

  void deleteItem(UUID id);

}
