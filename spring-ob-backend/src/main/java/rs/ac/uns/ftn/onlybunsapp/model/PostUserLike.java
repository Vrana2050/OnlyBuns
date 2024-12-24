package rs.ac.uns.ftn.onlybunsapp.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "post_user_likes")
public class PostUserLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_id", nullable = false)
    private Long postId;
    @Column(name = "like_date")
    private Timestamp likeDate;

    public PostUserLike() {}
    public PostUserLike(long userId, long postId) {
        this.userId = userId;
        this.postId = postId;
        likeDate = Timestamp.from(Instant.now());
    }
}
