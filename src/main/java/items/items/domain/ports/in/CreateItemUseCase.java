package items.items.domain.ports.in;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CreateItemUseCase {

  CreateItemResponse createItem(CreateItemCommand command);

  record CreateItemCommand(String title, String content) {

  }

  record CreateItemResponse(
      String id,
      String title,
      String content,
      int version,
      UUID ownerId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  ) {

  }

}
