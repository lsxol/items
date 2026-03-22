package items.items.domain.ports.in;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface GetItemHistoryQuery {

  List<ItemHistoryDto> getItemHistory(UUID itemId, UUID requesterId);

  record ItemHistoryDto(
      int revisionId,
      String revisionType,
      LocalDateTime timestamp,
      String changedBy,
      String title,
      String content
  ) {

  }

}
