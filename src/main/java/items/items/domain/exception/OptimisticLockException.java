package items.items.domain.exception;

import lombok.Getter;

@Getter
public class OptimisticLockException extends ItemException {

  private final int version;

  public OptimisticLockException(int version) {
    super("Konflikt wersji - ktoś inny zmodyfikował notatkę w międzyczasie. Aktualna wersja: " + version, ErrorCode.WRONG_VERSION);
    this.version = version;
  }

}
