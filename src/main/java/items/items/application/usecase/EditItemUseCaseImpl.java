package items.items.application.usecase;

import items.items.config.security.AuthHelper;
import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.EditItemUseCase;
import items.items.domain.ports.out.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EditItemUseCaseImpl implements EditItemUseCase {

  private final ItemRepository itemRepository;

  @Override
  public EditItemResponse editItem(EditItemCommand command) {

    Item item = itemRepository.findByIdAndDeletedFalse(new ItemId(command.id()))
        .orElseThrow(ItemExceptionUtil::itemNotExists);

    item.update(
        command.name(),
        command.content(),
        new UserId(AuthHelper.getAuth().userId())
    );

    var auditData = itemRepository.save(item, command.version());

    return new EditItemResponse(
        item.getId().value().toString(),
        item.getTitle(),
        item.getContent(),
        auditData.version(),
        auditData.updatedAt()
    );
  }

}
