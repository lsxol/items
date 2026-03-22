package items.items.infrastructure.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import items.items.domain.ports.in.AddUserUseCase;
import items.items.domain.ports.in.AddUserUseCase.AddUserCommand;
import items.items.domain.ports.in.AddUserUseCase.AddUserResponse;
import items.items.domain.ports.in.LoginUserUseCase;
import items.items.domain.ports.in.LoginUserUseCase.LoginUserCommand;
import items.items.infrastructure.security.LoginRateLimiter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Zarządzanie rejestracją i logowaniem użytkowników")
public class AuthController {

  private final AddUserUseCase addUserUseCase;
  private final LoginUserUseCase loginUseCase;
  private final LoginRateLimiter rateLimiter;

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Rejestracja nowego użytkownika", description = "Tworzy nowe konto w systemie. Wymaga unikalnego loginu (3-64 znaki) i silnego hasła.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Konto zostało pomyślnie utworzone"),
      @ApiResponse(responseCode = "400", description = "INVALID_USER / LOGIN_INVALID / PASSWORD_INVALID - Nieprawidłowe dane (np. za krótkie hasło lub login)"),
      @ApiResponse(responseCode = "409", description = "USER_ALREADY_EXISTS - Podany login jest już zajęty")
  })
  public AddUserResponse register(@RequestBody RegisterRequest request) {
    AddUserCommand command = new AddUserCommand(request.login(), request.password());
    return addUserUseCase.addUser(command);
  }

  @PostMapping("/login")
  @Operation(summary = "Logowanie użytkownika", description = "Autentykacja i pobranie tokena JWT. Endpoint jest chroniony przed atakami Brute Force (Rate Limiter).")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Pomyślnie zalogowano, zwraca token JWT oraz czas wygaśnięcia"),
      @ApiResponse(responseCode = "401", description = "INVALID_CREDENTIALS - Błędny login lub hasło"),
      @ApiResponse(responseCode = "429", description = "TOO_MANY_REQUESTS - Przekroczono limit prób logowania. Spróbuj ponownie później")
  })
  public ResponseEntity<?> login(
      @RequestBody LoginRequest request,
      HttpServletRequest httpRequest) {
    String ip = httpRequest.getRemoteAddr();
    var bucket = rateLimiter.resolveBucket(ip);
    if (!bucket.tryConsume(1)) {
      return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
          .header("Retry-After", "60")
          .body("Przekroczono limit prób logowania. Spróbuj ponownie za minutę.");
    }
    LoginUserCommand command = new LoginUserCommand(request.login(), request.password());

    return ResponseEntity.ok(loginUseCase.loginUser(command));
  }

  record RegisterRequest(String login, String password) {

  }

  record LoginRequest(String login, String password) {

  }

}