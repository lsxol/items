package items.items.infrastructure.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoginRateLimiter {

  @Value("${application.rate-limit.retry-after-seconds:60}")
  private long retryAfterSeconds;
  @Value("${application.rate-limit.capacity:5}")
  private long capacity;
  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  public Bucket resolveBucket(String ipAddress) {
    return buckets.computeIfAbsent(ipAddress, this::createNewBucket);
  }

  private Bucket createNewBucket(String ipAddress) {
    Bandwidth limit = Bandwidth.builder()
        .capacity(capacity)
        .refillIntervally(capacity, Duration.ofSeconds(retryAfterSeconds))
        .build();

    return Bucket.builder()
        .addLimit(limit)
        .build();
  }
}