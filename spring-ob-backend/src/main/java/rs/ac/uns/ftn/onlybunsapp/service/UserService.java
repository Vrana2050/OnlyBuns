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
    Page<AdminUserList> getUsers(Pageable pageable, String firstName, String lastName,
                                        String email, Integer minPosts, Integer maxPosts);

    public List<User> getFilteredUsers(PaginationRequest p);
}
