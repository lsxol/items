package items.items.domain.exception;

class ItemNotExistsException extends ItemException {

  ItemNotExistsException() {
    super("Notatka nie istnieje lub jest usunięta", ErrorCode.ITEM_NOT_EXISTS);
  }

}
