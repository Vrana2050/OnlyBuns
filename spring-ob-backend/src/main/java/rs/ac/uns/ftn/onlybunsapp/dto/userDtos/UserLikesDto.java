package rs.ac.uns.ftn.onlybunsapp.dto.userDtos;

import rs.ac.uns.ftn.onlybunsapp.model.User;

public class UserLikesDto {

    User user;
    long likeCount;

    public UserLikesDto(User user, Long likeCount) {
        this.user = user;
        this.likeCount = likeCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }


}
