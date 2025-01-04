package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.onlybunsapp.dto.AdminUserList;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);


    @Query("SELECT u FROM User u WHERE " +
            "(:firstName IS NULL OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))) AND " +
            "(:lastName IS NULL OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :lastName, '%'))) AND " +
            "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
            "(:minPosts IS NULL OR u.numberOfPosts >= :minPosts) AND " +
            "(:maxPosts IS NULL OR u.numberOfPosts <= :maxPosts)")
    Page<User> findAllWithFilters(@Param("firstName") String firstName,
                                  @Param("lastName") String lastName,
                                  @Param("email") String email,
                                  @Param("minPosts") Integer minPosts,
                                  @Param("maxPosts") Integer maxPosts,
                                  Pageable pageable);


    User findByEmail(String email);

    List<User> getAllByLastLoginDateBefore(Date date);

    @Query("SELECT CASE WHEN COUNT(f) > 0 THEN TRUE ELSE FALSE END " +
            "FROM User u JOIN u.following f " +
            "WHERE u.id = :followerId AND f.id = :followingId")
    boolean isFollowing(@Param("followerId") long followerId, @Param("followingId") long followingId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM following WHERE follower_id = :followerId AND following_id = :followingId", nativeQuery = true)
    void deleteFollowRelation(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

    //@Query("SELECT u FROM User u JOIN u.likedPosts p GROUP BY u ORDER BY COUNT(p) DESC")
    //List<User> getTop10UsersThatLikedTheMost();

    @Query("SELECT COUNT(u) FROM User u WHERE u.enabled = true")
    long countAllByEnabledIsTrue();

    @Modifying
    @Query("DELETE FROM User u WHERE u.enabled = false")
    void deleteByEnabledFalse();

}

