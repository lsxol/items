package items.items.domain.exception;

class CannotDeleteItemException extends ItemException {

  CannotDeleteItemException() {
    super("Użytkownik nie jest ownerem notatki", ErrorCode.USER_NOT_AUTHORIZED);
  }

}
