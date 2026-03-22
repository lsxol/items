package items.items.domain.exception;

class InvalidItemData extends ItemException {

  InvalidItemData() {
    super(ErrorCode.ITEM_INVALID);
  }

}
