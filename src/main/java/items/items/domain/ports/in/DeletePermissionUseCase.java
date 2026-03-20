package items.items.domain.ports.in;

import items.items.domain.model.ItemId;
import items.items.domain.model.UserId;

public interface DeletePermissionUseCase {

  void deletePermision(DeletePermissionCommand command);

  record DeletePermissionCommand(ItemId itemId, UserId userId) {

  }

}
