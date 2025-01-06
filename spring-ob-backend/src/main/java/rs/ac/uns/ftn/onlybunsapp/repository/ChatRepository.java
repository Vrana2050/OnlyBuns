package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("SELECT c FROM Chat c JOIN c.participants p WHERE p.id = :userId")
    List<Chat> findUserChats(Long userId);

    @Query("SELECT c FROM Chat c " +
            "WHERE c.isPrivate = true " +
            "AND size(c.participants) = 2 " +
            "AND EXISTS (SELECT p FROM c.participants p WHERE p.id = :user1Id) " +
            "AND EXISTS (SELECT p FROM c.participants p WHERE p.id = :user2Id)")
    Chat findPrivateChatBetweenUsers(@Param("user1Id") Long user1Id, @Param("user2Id") Long user2Id);

    @Query("SELECT DISTINCT c FROM Chat c JOIN c.participants p WHERE p.id IN :participantIds")
    List<Chat> findChatsByParticipantIds(@Param("participantIds") List<Long> participantIds);

}
