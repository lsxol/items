package items.items.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemPermission {

  private ItemPermissionId id;
  private UserId userId;
  private RoleEnum role;

  public static ItemPermission createNew(UserId userId, RoleEnum role) {
    return new ItemPermission(
        ItemPermissionId.generate(),
        userId,
        role
    );
  }

  public void changeRole(RoleEnum role) {
    this.role = role;
  }

}
