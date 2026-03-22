package items.items.infrastructure.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import items.items.domain.exception.ItemException;
import items.items.domain.exception.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ItemException.class)
  public ResponseEntity<ErrorResponse> handleItemException(ItemException ex) {
    HttpStatus httpStatus = mapCategoryToHttpStatus(ex.getErrorCode().getErrorCategory());
    Integer actualVersion = null;
    if (ex instanceof OptimisticLockException optEx) {
      actualVersion = optEx.getVersion();
    }

    ErrorResponse response = new ErrorResponse(
        ex.getErrorCode().name(),
        ex.getMessage(),
        actualVersion
    );

    return ResponseEntity.status(httpStatus).body(response);
  }

  private HttpStatus mapCategoryToHttpStatus(ItemException.ErrorCategory category) {
    return switch (category) {
      case BAD_REQUEST -> HttpStatus.BAD_REQUEST;
      case INVALID_CREDENTIALS -> HttpStatus.UNAUTHORIZED;
      case FORBIDDEN -> HttpStatus.FORBIDDEN;
      case NOT_FOUND -> HttpStatus.NOT_FOUND;
      case CONFLICT -> HttpStatus.CONFLICT;
      case TOO_MANY_REQUESTS -> HttpStatus.TOO_MANY_REQUESTS;
      case SYSTEM_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }

  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record ErrorResponse(
      String errorCode,
      String message,
      Integer actualVersion
  ) {

  }

}