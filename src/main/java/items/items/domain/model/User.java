package items.items.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

  private final UserId id;
  private final LoginWithPassword loginWithPassword;

  public static User createNew(LoginWithPassword loginWithPassword) {
    return new User(
        UserId.generate(),
        loginWithPassword
    );
  }

}
