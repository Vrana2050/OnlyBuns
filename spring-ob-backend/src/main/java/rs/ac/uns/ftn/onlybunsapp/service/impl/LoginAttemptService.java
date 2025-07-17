package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final long BLOCK_TIME_MS = 60 * 1000; // 1 minut

    private final ConcurrentHashMap<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String ip) {
        LoginAttempt attempt = attempts.get(ip);
        if(attempt != null)
            System.out.println("Broj pokusaja: " + attempt.getAttempts());

        if (attempt == null) {
            return false;
        }
        if (attempt.getAttempts() >= MAX_ATTEMPTS) {
            if (Instant.now().toEpochMilli() - attempt.getLastAttempt() < BLOCK_TIME_MS) {
                return true;
            } else {
                // Resetuje broj pokušaja nakon blokade
                attempts.remove(ip);
                return false;
            }
        }
        return false;
    }

    public void loginFailed(String ip) {
        attempts.compute(ip, (key, value) -> {
            if (value == null) {
                return new LoginAttempt(1, Instant.now().toEpochMilli());
            }
            return new LoginAttempt(value.getAttempts() + 1, Instant.now().toEpochMilli());
        });
    }

    public void loginSucceeded(String ip) {
        attempts.remove(ip); // Resetuj broj pokušaja nakon uspešne prijave
    }

    private static class LoginAttempt {
        private final int attempts;
        private final long lastAttempt;

        public LoginAttempt(int attempts, long lastAttempt) {
            this.attempts = attempts;
            this.lastAttempt = lastAttempt;
        }

        public int getAttempts() {
            return attempts;
        }

        public long getLastAttempt() {
            return lastAttempt;
        }
    }
}
