package items.items.domain.ports.out;

import items.items.domain.model.User;

public interface TokenProviderPort {

  String generateToken(User user);

}
