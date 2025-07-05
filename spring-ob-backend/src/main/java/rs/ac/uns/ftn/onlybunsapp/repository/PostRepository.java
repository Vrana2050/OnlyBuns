package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.domain.Pageable;
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


    @Query("SELECT COUNT(p) FROM Post p")
    int countAllPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE MONTH(p.postDate) = MONTH(CURRENT_DATE) AND YEAR(p.postDate) = YEAR(CURRENT_DATE)")
    int countThisMonthPosts();

    @Query("SELECT COUNT(p) FROM Post p WHERE p.postDate BETWEEN :startDate AND :endDate AND p.isDeleted = false AND p.isRestricted = false")
    int countPostsBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);

    @Query("SELECT p FROM Post p WHERE p.postDate >= CURRENT_DATE - 7 AND p.isDeleted = false AND p.isRestricted = false ORDER BY p.likes DESC")
    List<Post> getTop5MostLikedPostsLast7Days(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.isRestricted = false ORDER BY p.likes DESC")
    List<Post> getTop10MostLikedPosts(Pageable pageable);


    @Query("SELECT u AS user, COUNT(pul.id) AS likeCount FROM User u JOIN PostUserLike pul ON u.id = pul.userId WHERE pul.likeDate >= CURRENT_DATE - 7 GROUP BY u.id ORDER BY likeCount DESC")
    List<Object[]> findTop10UsersByLikesGivenThisWeek(Pageable pageable);



    @Query("SELECT COUNT(DISTINCT p.creator) FROM Post p WHERE p.isDeleted = false AND p.isRestricted = false")
    long countDistinctUsers();


    @Query("SELECT COUNT(DISTINCT p.creator) FROM Post p WHERE p.isDeleted = false AND p.isRestricted = false AND EXISTS (SELECT c FROM Comment c WHERE c.creator = p.creator)")
    long countUsersWithPostsAndComments();
}
