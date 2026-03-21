package items.items.domain.ports.in;

import items.items.domain.exception.ItemExceptionUtil;
import java.time.LocalDateTime;
import java.util.UUID;

public interface CreateItemUseCase {

  CreateItemResponse createItem(CreateItemCommand command);

  record CreateItemCommand(String title, String content) {
    public CreateItemCommand {
      if (title == null || title.isBlank() || title.length() > 255) {
        throw ItemExceptionUtil.itemInvalid();
      }
    }
  }

  record CreateItemResponse(
      UUID id,
      String title,
      String content,
      int version,
      UUID ownerId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  ) {

  }

}
