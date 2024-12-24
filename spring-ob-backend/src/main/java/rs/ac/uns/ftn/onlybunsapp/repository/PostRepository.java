package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rs.ac.uns.ftn.onlybunsapp.model.Post;
import rs.ac.uns.ftn.onlybunsapp.model.Role;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.sql.Timestamp;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>  {


    List<Post> findAllByCreatorInAndIsDeletedFalseAndIsRestrictedFalseOrderByPostDateDesc(List<User> creators);
    @Query("SELECT p FROM Post p WHERE p.creator.id = :userId AND p.isDeleted = false AND p.isRestricted = false")
    List<Post> findAllByUserIdAndNotDeletedAndNotRestricted(@Param("userId") Long userId);

    Post findById(long id);
    List<Post> findAllByCreatorInAndIsDeletedFalseAndIsRestrictedFalseAndPostDateAfter(
            List<User> creators,
            Timestamp lastLoginDate
    );
    Post save(Post post);
}
