package rs.ac.uns.ftn.onlybunsapp.dto.postDtos;

import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentReadDto;
import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;

import java.sql.Timestamp;
import java.util.List;

public class PostReadDto {
    public long id;
    public String description;
    public Timestamp postDate;
    public UserReadDto creator;
    public long likes;
    public int numOfComments;
    public String imageBase64;
    public List<CommentReadDto> comments;

    public long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getPostDate() {
        return postDate;
    }

    public UserReadDto getCreator() {
        return creator;
    }

    public long getLikes() {
        return likes;
    }

    public int getNumOfComments() {
        return numOfComments;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public List<CommentReadDto> getComments() {
        return comments;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostDate(Timestamp postDate) {
        this.postDate = postDate;
    }

    public void setCreator(UserReadDto creator) {
        this.creator = creator;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setNumOfComments(int numOfComments) {
        this.numOfComments = numOfComments;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public void setComments(List<CommentReadDto> comments) {
        this.comments = comments;
    }
}
