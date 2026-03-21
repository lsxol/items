package items.items.domain.ports.in;

import java.time.LocalDateTime;
import java.util.UUID;

public interface EditItemUseCase {

  EditItemResponse editItem(EditItemCommand command);

  record EditItemCommand(UUID id, String name, String content, int version) {

  }

  record EditItemResponse(String id, String name, String content, int version, LocalDateTime updatedAt) {

  }

}
