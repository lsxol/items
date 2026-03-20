package items.items.domain.exception;

class ItemInvalidException extends ItemException {

  ItemInvalidException() {
    super("Brakujące lub nieprawidłowe pola", ErrorCode.ITEM_INVALID);
  }

}
