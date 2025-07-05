package rs.ac.uns.ftn.onlybunsapp.metric;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CreatePostDurationMetric {
    private final ConcurrentHashMap<Long, Long> createPostDurations = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 24 * 60 * 60; // 24h u sekundama

    public CreatePostDurationMetric(MeterRegistry registry) {
        Gauge.builder("app.create.post.avg.duration", this::getAverageDuration)
                .description("Average duration of post creation requests in the last 24 hours")
                .register(registry);
    }

    // Izračunavanje prosečnog trajanja
    private double getAverageDuration() {
        long totalDuration = 0;
        long count = 0;
        for (Long duration : createPostDurations.values()) {
            totalDuration += duration;
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return (double) totalDuration / count;
    }

    // Dodavanje trajanja posta u mapu
    public void postCreated(Long duration) {
        createPostDurations.put(Instant.now().getEpochSecond(), duration);
    }

    // Čišćenje istečenih unosa svakih sat vremena
    @Scheduled(cron = "0 0 * * * ?") // Svaki sat
    private void cleanExpiredEntries() {
        long currentTimestamp = Instant.now().getEpochSecond();
        createPostDurations.entrySet().removeIf(entry -> currentTimestamp - entry.getKey() > EXPIRATION_TIME);
    }
}
