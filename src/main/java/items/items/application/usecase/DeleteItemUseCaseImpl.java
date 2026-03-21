package items.items.application.usecase;

import items.items.config.security.AuthHelper;
import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.DeleteItemUseCase;
import items.items.domain.ports.out.ItemRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteItemUseCaseImpl implements DeleteItemUseCase {

  private final ItemRepository itemRepository;

  @Override
  public void deleteItem(UUID id) {
    UserId requesterId = new UserId(AuthHelper.getAuth().userId());

    Item item = itemRepository.findByIdAndDeletedFalse(new ItemId(id))
        .orElseThrow(ItemExceptionUtil::itemNotExists);

    item.delete(requesterId);

    itemRepository.save(item);
  }

}
