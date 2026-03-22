package items.items.application.usecase;

import items.items.config.security.AuthHelper;
import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Item;
import items.items.domain.model.ItemId;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.ShareItemUseCase;
import items.items.domain.ports.out.ItemRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class ShareItemUseCaseImpl implements ShareItemUseCase {

 private final ItemRepository itemRepository;

 @Override
 @Transactional
 public ShareItemResponse shareItem(ShareItemCommand command) {
  UserId requesterId = new UserId(AuthHelper.getAuth().userId());
  Item item = itemRepository.findByIdAndDeletedFalse(new ItemId(command.itemId()))
      .orElseThrow(ItemExceptionUtil::itemNotExists);
  item.shareWith(new UserId(command.targetUserId()), command.role(), requesterId);
  itemRepository.save(item);
  return new ShareItemResponse(
      item.getId().value(),
      command.targetUserId(),
      command.role(),
      LocalDateTime.now()
  );
 }
}