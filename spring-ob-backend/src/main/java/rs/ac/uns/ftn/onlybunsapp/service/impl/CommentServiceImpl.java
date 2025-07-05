package rs.ac.uns.ftn.onlybunsapp.service.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentCreateDto;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.ratelimiter.CustomRateLimiter;
import rs.ac.uns.ftn.onlybunsapp.repository.CommentRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.PostRepository;
import rs.ac.uns.ftn.onlybunsapp.service.CommentService;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    //@RateLimiter(name = "comment", fallbackMethod = "standardFallback")
    @CustomRateLimiter(maxRequests = 5,durationInSeconds = 60)
    public Comment CreateComment(User creator, CommentCreateDto commentDto)throws AccessDeniedException {
        Post post= postRepository.findById(commentDto.postId);
        long postCreatorId = post.getCreator().getId();
        if(creator.getFollowing().stream().noneMatch(user->user.getId()== postCreatorId))
            throw new AccessDeniedException("You cannot comment on unfollowed accounts");
        Comment comment= new Comment();
        comment.setPost(post);
        comment.setCreator(creator);
        comment.setText(commentDto.comment);
        comment.setCreated(Timestamp.from(Instant.now()));
        post.setNumOfComments(post.getNumOfComments() + 1);
        postRepository.save(post);
        return commentRepository.save(comment);
    }
    public Comment standardFallback(User creator, CommentCreateDto commentDto, RequestNotPermitted rnp) {
        System.out.println("Prevazidjen broj poziva u ogranicenom vremenskom intervalu");
        throw rnp;
    }
}
