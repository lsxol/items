package items.items.domain.model;

import items.items.domain.exception.ItemExceptionUtil;

public record Login(String login) {

  public Login {
    if (login == null || login.isBlank() || login.length() > 64 || login.length() < 3) {
      throw ItemExceptionUtil.loginInvalid();
    }
  }

}
