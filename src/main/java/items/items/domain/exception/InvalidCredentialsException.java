package items.items.domain.exception;

class InvalidCredentialsException extends ItemException {

  InvalidCredentialsException() {
    super(ErrorCode.INVALID_CREDENTIALS);
  }

}
