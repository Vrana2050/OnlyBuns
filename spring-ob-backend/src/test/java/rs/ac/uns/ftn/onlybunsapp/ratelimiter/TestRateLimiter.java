package rs.ac.uns.ftn.onlybunsapp.ratelimiter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rs.ac.uns.ftn.onlybunsapp.exception.RateLimitExceededException;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class TestRateLimiter {
    @Autowired
    private UserService userFollowService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testRateLimiting() {
        System.out.println("\n========================================");
        System.out.println("🔍 STARTING RATE LIMIT TEST");
        System.out.println("========================================\n");

        // Create a follower user
        User follower = new User();
        follower.setUsername("testFollower");
        follower.setEmail("follower@test.com");
        follower = userRepository.save(follower);
        System.out.println("✅ Created follower user: " + follower.getUsername());

        // Create 51 users to follow
        List<User> usersToFollow = new ArrayList<>();
        for (int i = 0; i < 51; i++) {
            User user = new User();
            user.setUsername("testUser" + i);
            user.setEmail("user" + i + "@test.com");
            usersToFollow.add(userRepository.save(user));
        }
        System.out.println("✅ Created 51 users to follow");

        // Try to follow 50 users - should succeed
        for (int i = 0; i < 50; i++) {
            User finalFollower = follower;
            int finalI = i;
            assertDoesNotThrow(() -> {
                userFollowService.followUser(finalFollower.getId(), usersToFollow.get(finalI).getId());
            }, "Following user " + i + " should succeed");
            if (i % 10 == 0) {
                System.out.println("✅ Successfully followed " + (i + 1) + " users");
            }
        }
        System.out.println("✅ Successfully followed all 50 users");

        // Try to follow the 51st user
        User finalFollower1 = follower;
        assertThrows(RateLimitExceededException.class, () -> {
            userFollowService.followUser(finalFollower1.getId(), usersToFollow.get(50).getId());
        }, "Following the 51st user should fail due to rate limiting");
        System.out.println("✅ Rate limit correctly prevented 51st follow");

        // Verify the counts
        User updatedFollower = userRepository.findById(follower.getId()).get();
        assertEquals(50, updatedFollower.getNumberOfFollowing(),
                "Follower should be following exactly 50 users");
        System.out.println("✅ Verified follower is following exactly 50 users\n");
    }

    @Test
    void testConcurrentFollowing() throws InterruptedException {
        System.out.println("\n========================================");
        System.out.println("🔄 STARTING CONCURRENT FOLLOWING TEST");
        System.out.println("========================================\n");

        // Create target user to be followed
        User targetUser = new User();
        targetUser.setUsername("targetUser");
        targetUser.setEmail("target@test.com");
        targetUser.setNumberOfFollowers(0);
        targetUser = userRepository.save(targetUser);
        System.out.println("✅ Created target user to be followed");

        // Create two followers
        User follower1 = new User();
        follower1.setUsername("follower1");
        follower1.setEmail("follower1@test.com");
        follower1 = userRepository.save(follower1);

        User follower2 = new User();
        follower2.setUsername("follower2");
        follower2.setEmail("follower2@test.com");
        follower2 = userRepository.save(follower2);
        System.out.println("✅ Created two followers");

        // Store final references for use in lambda
        final Long targetUserId = targetUser.getId();
        final Long follower1Id = follower1.getId();
        final Long follower2Id = follower2.getId();

        // Create countdown latch to synchronize thread start
        CountDownLatch startLatch = new CountDownLatch(1);

        // Create threads for concurrent following
        Thread followThread1 = new Thread(() -> {
            try {
                startLatch.await(); // Wait for signal to start
                userFollowService.followUser(follower1Id, targetUserId);
                System.out.println("✅ Follower 1 completed following");
            } catch (Exception e) {
                fail("Follower 1 failed: " + e.getMessage());
            }
        });

        Thread followThread2 = new Thread(() -> {
            try {
                startLatch.await(); // Wait for signal to start
                userFollowService.followUser(follower2Id, targetUserId);
                System.out.println("✅ Follower 2 completed following");
            } catch (Exception e) {
                fail("Follower 2 failed: " + e.getMessage());
            }
        });

        // Start both threads
        followThread1.start();
        followThread2.start();

        // Signal threads to start simultaneously
        startLatch.countDown();

        // Wait for both threads to complete
        followThread1.join();
        followThread2.join();

        // Refresh target user from database
        User updatedTarget = userRepository.findById(targetUserId).orElseThrow();

        // Verify follower count
        assertEquals(2, updatedTarget.getNumberOfFollowers(),
                "Target user should have exactly 2 followers after concurrent follows");
        System.out.println("✅ Verified correct follower count\n");
    }
}