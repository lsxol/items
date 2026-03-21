package items.items.domain.exception;

class LoginInUseException extends ItemException {

  LoginInUseException() {
    super("Login zajęty", ErrorCode.USER_ALREADY_EXISTS);
  }

}
