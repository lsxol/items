package items.items.domain.exception;

class ItemInvalidException extends ItemException {

  ItemInvalidException() {
    super(ErrorCode.ITEM_INVALID);
  }

}
