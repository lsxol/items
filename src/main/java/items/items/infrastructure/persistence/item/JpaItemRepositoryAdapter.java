package items.items.infrastructure.persistence.item;

import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import items.items.domain.model.UserId;
import items.items.domain.ports.out.ItemRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaItemRepositoryAdapter implements ItemRepository {

  private final SpringDataItemRepository itemRepository;

  @Override
  public SavedItemAuditData save(Item item) {
    ItemEntity entity = itemRepository.findById(item.getId().value())
        .orElseGet(ItemEntity::new);
    entity.setId(item.getId().value());
    entity.setOwnerId(item.getOwnerId().value());
    entity.setTitle(item.getTitle());
    entity.setContent(item.getContent());
    entity.setDeleted(item.isDeleted());

    mapPermissionsToEntity(item, entity);

    entity = itemRepository.saveAndFlush(entity);

    return new SavedItemAuditData(
        entity.getVersion(),
        entity.getCreatedAt(),
        entity.getUpdatedAt()
    );
  }

  @Override
  public SavedItemAuditData save(Item item, int version) {

    ItemEntity entity = itemRepository.findById(item.getId().value())
        .orElseThrow(ItemExceptionUtil::itemNotExists);

    if (entity.getVersion() != version) {
      throw ItemExceptionUtil.optimisticLock(entity.getVersion());
    }

    entity.setTitle(item.getTitle());
    entity.setContent(item.getContent());

    mapPermissionsToEntity(item, entity);

    entity = itemRepository.saveAndFlush(entity);

    return new SavedItemAuditData(entity.getVersion(), entity.getCreatedAt(), entity.getUpdatedAt());
  }

  @Override
  public Optional<Item> findByIdAndDeletedFalse(ItemId id) {
    return itemRepository.findByIdAndDeletedFalse(id.value())
        .map(itemEntity -> mapToDomain(id, itemEntity));
  }

  private void mapPermissionsToEntity(Item item, ItemEntity entity) {
    if (item.getPermissions() == null) {
      return;
    }

    List<UUID> domainUserIds = item.getPermissions().stream()
        .map(p -> p.getUserId().value())
        .toList();

    if (entity.getPermissions() != null) {
      entity.getPermissions().removeIf(permEntity ->
          !domainUserIds.contains(permEntity.getUserId())
      );
    }

    for (var domenaPerm : item.getPermissions()) {
      Optional<ItemPermissionEntity> existingPerm = entity.getPermissions().stream()
          .filter(permission -> permission.getUserId().equals(domenaPerm.getUserId().value()))
          .findFirst();

      if (existingPerm.isPresent()) {
        existingPerm.get().setRole(domenaPerm.getRole());
      } else {
        ItemPermissionEntity permEntity = new ItemPermissionEntity();
        permEntity.setId(domenaPerm.getId() != null ? domenaPerm.getId().value() : UUID.randomUUID());
        permEntity.setUserId(domenaPerm.getUserId().value());
        permEntity.setRole(domenaPerm.getRole());

        entity.addPermission(permEntity);
      }
    }
  }

  private Item mapToDomain(ItemId id, ItemEntity itemEntity) {
    return new Item(id,
        new UserId(itemEntity.getOwnerId()),
        itemEntity.getTitle(),
        itemEntity.getContent(),
        itemEntity.isDeleted(),
        new ArrayList<>());
  }

}
