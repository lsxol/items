package items.items.domain.exception;

class ItemNotExistsException extends ItemException {

  ItemNotExistsException() {
    super("Notatka nie istnieje lub jest usunięta", ErrorCode.USER_NOT_AUTHORIZED);
  }

}
