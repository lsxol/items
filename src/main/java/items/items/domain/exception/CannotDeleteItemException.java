package items.items.domain.exception;

class CannotDeleteItemException extends ItemException {

  CannotDeleteItemException() {
    super(ErrorCode.USER_NOT_AUTHORIZED);
  }

}
