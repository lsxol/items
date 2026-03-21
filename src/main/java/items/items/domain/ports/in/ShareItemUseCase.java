package items.items.domain.ports.in;

import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.RoleEnum;
import java.time.LocalDateTime;
import java.util.UUID;

public interface ShareItemUseCase {

  ShareItemResponse shareItem(ShareItemCommand command);

  record ShareItemCommand(UUID itemId, UUID targetUserId, RoleEnum role) {
    public ShareItemCommand {
      if (itemId == null || targetUserId == null || role == null) {
        throw ItemExceptionUtil.invalidItemData();
      }
    }
  }

  record ShareItemResponse(UUID itemId, UUID userId, RoleEnum role, LocalDateTime grantedAt) {
  }
}