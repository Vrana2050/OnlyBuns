package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.Role;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>  {


    List<Post> findAllByCreatorInAndIsDeletedFalseAndIsRestrictedFalseOrderByPostDateDesc(List<User> creators);
}
