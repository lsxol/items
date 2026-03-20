package items.items.domain.exception;

class LoginInvalidException extends ItemException {

  LoginInvalidException() {
    super("Login nie spełnia wymagań, powinien mieć minimalnie 3 znaki i maksymalnie 64", ErrorCode.LOGIN_INVALID);
  }

}
