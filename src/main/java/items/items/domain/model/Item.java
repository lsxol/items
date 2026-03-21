package items.items.domain.model;

import items.items.domain.exception.ItemExceptionUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Item {

  private ItemId id;
  private UserId ownerId;
  private String title;
  private String content;
  private boolean deleted;
  private List<ItemPermission> permissions;

  public static Item createNew(UserId ownerId, String title, String content) {
    if (title == null || title.isBlank() || title.length() > 255) {
      throw ItemExceptionUtil.itemInvalid();
    }
    return new Item(
        ItemId.generate(),
        ownerId,
        title,
        content,
        false,
        new ArrayList<>()
    );
  }

  public void update(String newTitle, String newContent, UserId editorId) {
    if (newTitle == null || newTitle.isBlank()) {
      throw ItemExceptionUtil.itemInvalid();
    }
    if (!canEdit(editorId)) {
      throw ItemExceptionUtil.cannotEditItem();
    }
    if (this.deleted) {
      throw ItemExceptionUtil.itemNotExists();
    }
    this.title = newTitle;
    this.content = newContent;
  }

  public void delete(UserId userId) {
    if (!canDelete(userId)) {
      throw ItemExceptionUtil.cannotDeleteItem();

    }
    if (this.deleted) {
      throw ItemExceptionUtil.itemNotExists();
    }
    this.deleted = true;
  }

  public void shareWith(UserId targetUserId, RoleEnum role, UserId requesterId) {
    if (!this.ownerId.equals(requesterId)) {
      throw ItemExceptionUtil.cannotEditItem();
    }
    if (this.ownerId.equals(targetUserId)) {
      throw ItemExceptionUtil.invalidItemData();
    }
    Optional<ItemPermission> existingPermission = this.permissions.stream()
        .filter(p -> p.getUserId().equals(targetUserId))
        .findFirst();
    if (existingPermission.isPresent()) {
      existingPermission.get().changeRole(role);
    } else {
      this.permissions.add(ItemPermission.createNew(targetUserId, role));
    }
  }

  public void revokeAccess(UserId targetUserId, UserId requesterId) {
    if (!this.ownerId.equals(requesterId)) {
      throw ItemExceptionUtil.cannotEditItem();
    }

    if (this.ownerId.equals(targetUserId)) {
      throw ItemExceptionUtil.invalidItemData();
    }
    this.permissions.removeIf(p -> p.getUserId().equals(targetUserId));

  }

  private boolean canEdit(UserId userId) {
    return ownerId.equals(userId) || permissions.stream()
        .anyMatch(itemPermission -> itemPermission.getUserId().equals(userId) && itemPermission.getRole().canEdit());
  }

  private boolean canDelete(UserId userId) {
    return userId.equals(ownerId);
  }

}
