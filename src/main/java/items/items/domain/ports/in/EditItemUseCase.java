package items.items.domain.ports.in;

import java.time.LocalDateTime;

public interface EditItemUseCase {

  EditItemResponse editItem(EditItemCommand command);

  record EditItemCommand(String name, String content, int version) {

  }

  record EditItemResponse(String id, String name, String content, int version, LocalDateTime updatedAt) {

  }

}
