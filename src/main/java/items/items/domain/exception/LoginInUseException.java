package items.items.domain.exception;

class LoginInUseException extends ItemException {

  LoginInUseException() {
    super(ErrorCode.USER_ALREADY_EXISTS);
  }

}
