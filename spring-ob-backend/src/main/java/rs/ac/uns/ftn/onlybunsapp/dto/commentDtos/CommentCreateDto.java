package rs.ac.uns.ftn.onlybunsapp.dto.commentDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentCreateDto {
    @NotNull
    public Integer postId;
    @NotBlank
    public String comment;
    public int getPostId(){return postId;}
    public String getComment(){return comment;}
    public void setPostId(Integer postId){this.postId = postId;}
    public void setComment(String comment){this.comment = comment;}
}
