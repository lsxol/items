package items.items.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import items.items.domain.model.User;
import items.items.domain.ports.out.TokenProviderPort;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class JwtService implements TokenProviderPort {

  private final SecretKey secretKey;
  private final long expirationTime;

  public JwtService(@Value("${application.jwt.secret-key}") String secretKey,
      @Value("${application.jwt.expiration-ms}") long expirationTime) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    this.expirationTime = expirationTime;
  }

  @Override
  public TokenInfo generateToken(User user) {
    String token = Jwts.builder()
        .subject(user.getLoginWithPassword().login().login())
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + expirationTime))
        .claim("userId", user.getId().value().toString())
        .signWith(secretKey)
        .compact();
    int expirationTimeInSeconds = (int) (expirationTime / 1000);
    return new TokenInfo(token, expirationTimeInSeconds);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  public String extractLogin(String token) {
    return extractAllClaims(token).getSubject();
  }

  public UUID extractUserId(String token) {
    String userIdString = extractAllClaims(token).get("userId", String.class);
    return UUID.fromString(userIdString);
  }

  public boolean isTokenValid(String token, String login) {
    final String tokenLogin = extractLogin(token);
    return (tokenLogin.equals(login)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

}
