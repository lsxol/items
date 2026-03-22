package items.items.application.usecase;

import items.items.config.security.AuthHelper;
import items.items.domain.model.Item;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.CreateItemUseCase;
import items.items.domain.ports.out.ItemRepository;
import items.items.domain.ports.out.ItemRepository.SavedItemAuditData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class CreateItemUseCaseImpl implements CreateItemUseCase {

  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public CreateItemResponse createItem(CreateItemCommand command) {
    Item newItem = Item.createNew(new UserId(AuthHelper.getAuth().userId()), command.title(), command.content());
    SavedItemAuditData auditData = itemRepository.save(newItem);
    return new CreateItemResponse(
        newItem.getId().value(),
        newItem.getTitle(),
        newItem.getContent(),
        auditData.version(),
        newItem.getOwnerId().value(),
        auditData.createdAt(),
        auditData.updatedAt()
    );
  }

}

