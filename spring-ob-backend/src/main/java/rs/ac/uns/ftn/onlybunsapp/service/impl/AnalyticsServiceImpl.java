package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.repository.CommentRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.PostRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.AnalyticsService;
import rs.ac.uns.ftn.onlybunsapp.service.UserService;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserService userService;

    public Map<String, Integer> getActivityCounts(Timestamp startDate, Timestamp endDate) {
        int posts = postRepository.countPostsBetweenDates(startDate, endDate);
        int comments = commentRepository.countCommentsBetweenDates(startDate, endDate);

        return Map.of(
                "posts", posts,
                "comments", comments
        );
    }

    public Map<String, Double> getUserActivityStats() {
        long totalUsers = userService.findAll().size();
        long postUsers = postRepository.countDistinctUsers();
        long commentUsers = commentRepository.countDistinctUsers();
        long both = postRepository.countUsersWithPostsAndComments();

        long onlyCommentUsers = commentUsers - both;
        long inactiveUsers = totalUsers - (postUsers + onlyCommentUsers);

        Map<String, Double> stats = new HashMap<>();
        stats.put("PostUsers", (postUsers / (double) totalUsers) * 100);
        stats.put("CommentUsers", (onlyCommentUsers / (double) totalUsers) * 100);
        stats.put("InactiveUsers", (inactiveUsers / (double) totalUsers) * 100);

        System.out.println("User Activity Stats: " + stats);
        return stats;
    }


}
