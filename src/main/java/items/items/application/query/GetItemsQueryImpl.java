package items.items.application.query;

import items.items.domain.ports.in.GetItemsQuery;
import items.items.infrastructure.persistence.item.ItemEntity;
import items.items.infrastructure.persistence.item.SpringDataItemReadRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class GetItemsQueryImpl implements GetItemsQuery {

  private final SpringDataItemReadRepository itemRepository;

  @Override
  @Transactional(readOnly = true)
  public PaginatedItemsResponse getItems(UUID userId, int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<ItemEntity> entityPage = itemRepository.findAvailableItemsForUser(userId, pageRequest);

    List<ItemDto> dtos = entityPage.getContent().stream()
        .map(this::mapToDto)
        .toList();

    return new PaginatedItemsResponse(
        dtos,
        entityPage.getNumber(),
        entityPage.getTotalPages(),
        entityPage.getTotalElements()
    );
  }

  private ItemDto mapToDto(ItemEntity entity) {
    return new ItemDto(
        entity.getId(),
        entity.getTitle(),
        entity.getContent(),
        entity.getVersion(),
        entity.getOwnerId(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }

}
