package rs.ac.uns.ftn.onlybunsapp.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class ActiveUserMetric {
    private final ConcurrentHashMap<String, Long> activeUsers = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 24 * 60 * 60; // 24h u sekundama

    public ActiveUserMetric(MeterRegistry registry) {
        Gauge.builder("app.active.users", activeUsers, map -> map.size())
                .description("Number of active users in the last 24 hours")
                .register(registry);
    }
    public void userLoggedIn(String jwt) {
        activeUsers.put(jwt, Instant.now().getEpochSecond());
    }
    public void userLoggedOut(String jwt) {
        activeUsers.remove(jwt);
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void removeExpiredUsers() {
        long currentTime = Instant.now().getEpochSecond();
        activeUsers.entrySet().removeIf(entry -> currentTime - entry.getValue() > EXPIRATION_TIME);
    }
}
