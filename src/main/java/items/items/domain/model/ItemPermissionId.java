package items.items.domain.model;

import java.util.UUID;

public record ItemPermissionId(UUID value) {

  public static ItemPermissionId generate() {
    return new ItemPermissionId(UUID.randomUUID());
  }

}
