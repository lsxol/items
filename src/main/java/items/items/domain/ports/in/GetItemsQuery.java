package items.items.domain.ports.in;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetItemsQuery {

  PaginatedItemsResponse getItems(UUID userId, int page, int size);

  record ItemDto(
      UUID id,
      String title,
      String content,
      int version,
      UUID ownerId,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
  ) {

  }

  record PaginatedItemsResponse(
      List<ItemDto> items,
      int currentPage,
      int totalPages,
      long totalElements
  ) {

  }

}
