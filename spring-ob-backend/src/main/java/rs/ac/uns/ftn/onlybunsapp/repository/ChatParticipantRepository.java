package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.ChatParticipant;

import java.util.List;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    public ChatParticipant findByChatIdAndUserId(Long chatId, Long userId);

    @Query("SELECT DISTINCT cp.chat FROM ChatParticipant cp WHERE cp.user.id = :userId")
    List<Chat> findDistinctChatsByUserId(@Param("userId") Long userId);
}
