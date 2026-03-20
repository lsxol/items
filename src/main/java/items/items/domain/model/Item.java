package items.items.domain.model;

import items.items.domain.exception.ItemExceptionUtil;
import java.util.List;
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
    if (title == null || title.isBlank()) {
      throw ItemExceptionUtil.itemInvalidException();
    }
    return new Item(
        ItemId.generate(),
        ownerId,
        title,
        content,
        false,
        List.of()
    );
  }

  public void update(String newTitle, String newContent, UserId editorId) {
    if (newTitle == null || newTitle.isBlank()) {
      throw ItemExceptionUtil.itemInvalidException();
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

  private boolean canEdit(UserId userId) {
    return permissions.stream()
        .anyMatch(itemPermission -> itemPermission.getUserId().equals(userId) && itemPermission.getRole().canEdit());
  }

  private boolean canDelete(UserId userId) {
    return userId.equals(ownerId);
  }

}
