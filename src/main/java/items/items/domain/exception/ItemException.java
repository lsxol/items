package items.items.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ItemException extends RuntimeException {
  private final ErrorCode errorCode;

  public ItemException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

  @Getter
  @AllArgsConstructor
  public enum ErrorCode {
    INVALID_USER(ErrorCategory.BAD_REQUEST),
    LOGIN_INVALID(ErrorCategory.BAD_REQUEST),
    INVALID_CREDENTIALS(ErrorCategory.INVALID_CREDENTIALS),
    PASSWORD_INVALID(ErrorCategory.BAD_REQUEST),
    ITEM_INVALID(ErrorCategory.BAD_REQUEST),
    USER_ALREADY_EXISTS(ErrorCategory.CONFLICT),
    USER_NOT_AUTHORIZED(ErrorCategory.FORBIDDEN),
    TOO_MANY_REQUESTS(ErrorCategory.TOO_MANY_REQUESTS),
    SYSTEM_ERROR(ErrorCategory.SYSTEM_ERROR);
    private final ErrorCategory errorCategory;
  }

  public enum ErrorCategory {
    NOT_FOUND,
    CONFLICT,
    BAD_REQUEST,
    FORBIDDEN,
    SYSTEM_ERROR,
    INVALID_CREDENTIALS,
    TOO_MANY_REQUESTS
  }

}
