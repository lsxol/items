package items.items.domain.exception;

class OptimisticLockException extends ItemException {

  OptimisticLockException(int version) {
    super("Konflikt wersji - ktoś inny zmodyfikował notatkę w międzyczasie. Aktualna wersja: " + version, ErrorCode.WRONG_VERSION);
  }

}
