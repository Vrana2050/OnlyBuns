package rs.ac.uns.ftn.onlybunsapp.ratelimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class RateLimiterFollow {
    private final Map<Long, FollowCounter> followCounters = new ConcurrentHashMap<>();
    private static final int FOLLOW_LIMIT = 50;
    private static final long RESET_INTERVAL = 60 * 1000; // 1 minut u milisekundama

    public boolean canFollow(long userId) {
        FollowCounter counter = followCounters.computeIfAbsent(userId, FollowCounter::new);
        synchronized (counter) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - counter.getLastResetTime() > RESET_INTERVAL) {
                counter.reset(currentTime);
            }
            return counter.getFollowCount() < FOLLOW_LIMIT;
        }
    }

    public void incrementFollowCount(long userId) {
        FollowCounter counter = followCounters.computeIfAbsent(userId, FollowCounter::new);
        synchronized (counter) {
            counter.increment();
        }
    }


    private static class FollowCounter {
        private int followCount;
        private long lastResetTime;

        public FollowCounter(long userId) {
            this.followCount = 0;
            this.lastResetTime = System.currentTimeMillis();
        }

        public int getFollowCount() {
            return followCount;
        }

        public long getLastResetTime() {
            return lastResetTime;
        }

        public void reset(long currentTime) {
            this.followCount = 0;
            this.lastResetTime = currentTime;
        }

        public void increment() {
            this.followCount++;
        }
    }
}