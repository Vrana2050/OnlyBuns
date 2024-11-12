package rs.ac.uns.ftn.onlybunsapp.model;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POSTS")
public class Post {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="creator_Id", referencedColumnName = "id" , nullable = false)
    private User creator;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id", referencedColumnName = "id", nullable = false)
    private Location location;

    @Column(name = "folder_path")
    private String folderPath;

    @Column(name = "post_date")
    private Timestamp postDate;

    @ManyToMany
    @JoinTable(
            name = "post_user_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> likedBy;

    @Column(name = "likes")
    private long likes;

    @Column(name = "num_of_comments")
    private int numOfComments;


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    public Post() {}

    public void addLike() {
        likes=likes+1;
    }
    public void removeLike() {
        likes=likes-1;
    }

    public void publish (User user)
    {
        this.creator = user;
        setPostDate(new Timestamp(System.currentTimeMillis()));
    }

    public User getCreator() {
        return creator;
    }

    public List<User> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(List<User> likedBy) {
        this.likedBy = likedBy;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public Location getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
