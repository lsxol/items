package items.items.application.usecase;

import items.items.config.security.AuthHelper;
import items.items.domain.exception.ItemExceptionUtil;
import items.items.domain.model.Item;
import items.items.domain.model.UserId;
import items.items.domain.ports.in.DeletePermissionUseCase;
import items.items.domain.ports.out.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class DeletePermissionUseCaseImpl implements DeletePermissionUseCase {

  private final ItemRepository itemRepository;

  @Override
  @Transactional
  public void deletePermission(DeletePermissionCommand command) {
    UserId requesterId = new UserId(AuthHelper.getAuth().userId());
    Item item = itemRepository.findByIdAndDeletedFalse(command.itemId())
        .orElseThrow(ItemExceptionUtil::itemNotExists);
    item.revokeAccess(command.userId(), requesterId);
    itemRepository.save(item);
  }

}
