package items.items.domain.exception;

class CannotEditItemException extends ItemException {

  CannotEditItemException() {
    super("Brak uprawnień do edycji tej notatki", ErrorCode.USER_NOT_AUTHORIZED);
  }

}
