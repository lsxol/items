package items.items.domain.exception;

class PasswordInvalidException extends ItemException {

  PasswordInvalidException() {
    super("Hasło musi mieć conajmniej 8 znaków", ErrorCode.PASSWORD_INVALID);
  }

}
