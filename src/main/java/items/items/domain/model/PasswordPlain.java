package items.items.domain.model;

import items.items.domain.exception.ItemExceptionUtil;

public record PasswordPlain(String password) {

  public PasswordPlain {
    if (password == null || password.isBlank() || password.length() < 8) {
      throw ItemExceptionUtil.passwordInvalid();
    }
  }

}
