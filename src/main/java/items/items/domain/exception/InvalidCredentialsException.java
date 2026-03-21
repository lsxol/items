package items.items.domain.exception;

class InvalidCredentialsException extends ItemException {

  InvalidCredentialsException() {
    super("Złe dane logowania", ErrorCode.INVALID_CREDENTIALS);
  }

}
