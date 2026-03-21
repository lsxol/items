package items.items.domain.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class ItemExceptionUtil {

  public static ItemException loginInvalid() {
    return new LoginInvalidException();
  }

  public static ItemException loginInUse() {
    return new LoginInUseException();
  }

  public static ItemException passwordInvalid() {
    return new PasswordInvalidException();
  }

  public static ItemException userDataInvalid() {
    return new UserDataInvalidException();
  }

  public static ItemException itemInvalid() {
    return new ItemInvalidException();
  }

  public static ItemException cannotEditItem() {
    return new CannotEditItemException();
  }

  public static ItemException itemNotExists() {
    return new ItemNotExistsException();
  }

  public static ItemException cannotDeleteItem() {
    return new CannotDeleteItemException();
  }

  public static ItemException invalidCredentials() {
    return new InvalidCredentialsException();
  }

}
