package rs.ac.uns.ftn.onlybunsapp.model;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "COMMENTS")
public class Comment {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="creator_Id", referencedColumnName = "id" , nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", referencedColumnName = "id", nullable = false)
    private Post post;

    @Column(name = "created")
    private Timestamp created;

    public Comment(Timestamp created, User creator, String text, long id) {
        this.created = created;
        this.creator = creator;
        this.text = text;
        this.id = id;
    }
    public Comment() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;

    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
