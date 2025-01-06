package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.onlybunsapp.model.Message;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByCreatedAtDesc(Long chatId);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.createdAt DESC")
    List<Message> findTop1ByChatId(@Param("chatId") Long chatId, Pageable pageable);

    // Fetch the 10 most recent messages before a specific timestamp
    List<Message> findTop10ByChatIdAndCreatedAtLessThanOrderByCreatedAtDesc(Long chatId, Timestamp createdAt);

    // Fetch all messages after or equal to a specific timestamp
    List<Message> findByChatIdAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(Long chatId, Timestamp createdAt);



}
