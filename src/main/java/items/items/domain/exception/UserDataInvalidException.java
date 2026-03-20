package items.items.domain.exception;

class UserDataInvalidException extends ItemException {

  UserDataInvalidException() {
    super("Brak uzupełnionych danych użytkownika", ErrorCode.INVALID_USER);
  }

}
