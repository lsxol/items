package items.items.domain.ports.in;

import items.items.domain.model.ItemId;
import items.items.domain.model.RoleEnum;
import items.items.domain.model.UserId;
import java.time.LocalDateTime;

public interface ShareItemUseCase {

  ShareItemResponse shareItem(ShareItemCommand command);

  record ShareItemCommand(UserId id, RoleEnum role) {

  }

  record ShareItemResponse(ItemId id, UserId userId, RoleEnum role, LocalDateTime grantedAt) {

  }

}
