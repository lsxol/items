package items.items.domain.exception;

class ItemNotExistsException extends ItemException {

  ItemNotExistsException() {
    super(ErrorCode.ITEM_NOT_EXISTS);
  }

}
