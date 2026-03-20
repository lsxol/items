package items.items.domain.model;

import java.util.UUID;

public record ItemId(UUID value) {

  public static ItemId generate() {
    return new ItemId(UUID.randomUUID());
  }

}
