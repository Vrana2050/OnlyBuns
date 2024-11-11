package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.onlybunsapp.model.PostUserLike;

@Repository
public interface LikeRepository extends JpaRepository<PostUserLike, Long> {
    PostUserLike findByPostIdAndUserId(Long postId, Long userId);
}
