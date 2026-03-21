package items.items.domain.exception;

class InvalidItemData extends ItemException {

  InvalidItemData() {
    super("Nieprawidłowa rola lub brakujące pola", ErrorCode.ITEM_INVALID);
  }

}
