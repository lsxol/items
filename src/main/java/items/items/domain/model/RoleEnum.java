package items.items.domain.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleEnum {
  VIEWER, EDITOR, OWNER;

  public boolean canEdit() {
    return this.equals(EDITOR) || this.equals(OWNER);
  }
}
