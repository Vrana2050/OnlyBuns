package rs.ac.uns.ftn.onlybunsapp.service;

import java.util.List;

import rs.ac.uns.ftn.onlybunsapp.dto.UserRequest;
import rs.ac.uns.ftn.onlybunsapp.model.User;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll ();
	User save(UserRequest userRequest);
    User update(User updatedUser);
    boolean activateUser(long userId);
    User findByEmail(String email);
}
