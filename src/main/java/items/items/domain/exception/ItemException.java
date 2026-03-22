package items.items.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ItemException extends RuntimeException {
  private final ErrorCode errorCode;

  public ItemException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

  public ItemException(String customMessage, ErrorCode errorCode) {
    super(customMessage);
    this.errorCode = errorCode;
  }

  @Getter
  @AllArgsConstructor
  public enum ErrorCode {
    INVALID_USER(ErrorCategory.BAD_REQUEST, "Nieprawidłowy użytkownik"),
    ITEM_NOT_EXISTS(ErrorCategory.NOT_FOUND, "Notatka nie istnieje lub została usunięta"),
    LOGIN_INVALID(ErrorCategory.BAD_REQUEST, "Login nie spełnia wymagań, powinien mieć minimalnie 3 znaki i maksymalnie 64"),
    INVALID_CREDENTIALS(ErrorCategory.INVALID_CREDENTIALS, "Błędny login lub hasło"),
    WRONG_VERSION(ErrorCategory.CONFLICT, "Konflikt wersji - pobierz notatkę ponownie"),
    PASSWORD_INVALID(ErrorCategory.BAD_REQUEST, "Hasło nie spełnia wymagań bezpieczeństwa"),
    ITEM_INVALID(ErrorCategory.BAD_REQUEST, "Dane notatki są nieprawidłowe"),
    USER_ALREADY_EXISTS(ErrorCategory.CONFLICT, "Podany login jest już zajęty"),
    USER_NOT_AUTHORIZED(ErrorCategory.FORBIDDEN, "Brak uprawnień do wykonania tej operacji"),
    TOO_MANY_REQUESTS(ErrorCategory.TOO_MANY_REQUESTS, "Przekroczono limit zapytań. Spróbuj ponownie później"),
    SYSTEM_ERROR(ErrorCategory.SYSTEM_ERROR, "Wewnętrzny błąd systemu");
    private final ErrorCategory errorCategory;
    private final String message;
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
