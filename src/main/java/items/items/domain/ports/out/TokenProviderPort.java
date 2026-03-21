package items.items.domain.ports.out;

import items.items.domain.model.User;

public interface TokenProviderPort {

  TokenInfo generateToken(User user);

  record TokenInfo(String token, int expiresInSeconds) {}

}
