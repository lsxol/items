package items.items.application.query;

import items.items.config.jpa.AuditableRevisionEntity;
import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.ports.in.GetItemHistoryQuery;
import items.items.infrastructure.persistence.item.ItemEntity;
import items.items.infrastructure.persistence.item.SpringDataItemReadRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class GetItemHistoryQueryImpl implements GetItemHistoryQuery {

  private final EntityManager entityManager;
  private final SpringDataItemReadRepository readRepository;

  @Override
  @Transactional(readOnly = true)
  public List<ItemHistoryDto> getItemHistory(UUID itemId, UUID requesterId) {

    readRepository.findById(itemId).ifPresentOrElse(
        item -> verifyAccess(item, requesterId),
        () -> {
          throw ItemExceptionUtil.itemNotExists();
        }
    );

    AuditReader auditReader = AuditReaderFactory.get(entityManager);

    List<Object[]> revisions = auditReader.createQuery()
        .forRevisionsOfEntity(ItemEntity.class, false, true)
        .add(AuditEntity.id().eq(itemId))
        .addOrder(AuditEntity.revisionNumber().desc())
        .getResultList();

    return revisions.stream()
        .map(this::mapToHistoryDto)
        .toList();
  }

  private void verifyAccess(ItemEntity item, UUID requesterId) {
    boolean isOwner = item.getOwnerId().equals(requesterId);
    boolean hasPermission = item.getPermissions().stream()
        .anyMatch(p -> p.getUserId().equals(requesterId));

    if (!isOwner && !hasPermission) {
      throw ItemExceptionUtil.cannotGetItem();
    }
  }

  private ItemHistoryDto mapToHistoryDto(Object[] revisionData) {
    ItemEntity itemEntity = (ItemEntity) revisionData[0];
    AuditableRevisionEntity revisionEntity = (AuditableRevisionEntity) revisionData[1];
    RevisionType revisionType = (RevisionType) revisionData[2];

    return new ItemHistoryDto(
        revisionEntity.getId(),
        revisionType.name(),
        convertTimestampToLocalDateTime(revisionEntity.getTimestamp()),
        revisionEntity.getChangedBy(),
        itemEntity.getTitle(),
        itemEntity.getContent()
    );
  }

  private LocalDateTime convertTimestampToLocalDateTime(long timestamp) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
  }

}