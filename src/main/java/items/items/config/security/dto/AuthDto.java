package items.items.config.security.dto;

import java.util.UUID;

public record AuthDto(UUID userId, String login) {

}
