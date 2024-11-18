package rs.ac.uns.ftn.onlybunsapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.dto.PaginationRequest;
import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);

    public Page<User> probaPaginacije(PaginationRequest p);
    User update(User updatedUser);
    boolean activateUser(long userId);
    User findByEmail(String email);

    public boolean isFollowing(long followerId, long followingId);
    public void followUser(long followerId, long followingId);
    public void unfollowUser(long followerId, long followingId);

}
