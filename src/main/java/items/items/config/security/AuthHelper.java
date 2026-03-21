package items.items.config.security;

import items.items.config.security.dto.AuthDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthHelper {

  public static AuthDto getAuth() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.getPrincipal() instanceof AuthDto authDto) {
      return authDto;
    }

    return new AuthDto(null, null);
  }
}