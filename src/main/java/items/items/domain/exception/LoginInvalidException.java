package items.items.domain.exception;

class LoginInvalidException extends ItemException {

  LoginInvalidException() {
    super(ErrorCode.LOGIN_INVALID);
  }

}
