package items.items.domain.exception;

class CannotGetItemException extends ItemException {

  CannotGetItemException() {
    super(ErrorCode.USER_NOT_AUTHORIZED);
  }

}
