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

    @Query("SELECT c FROM Chat c WHERE " +
            "(SELECT COUNT(cp) FROM ChatParticipant cp WHERE cp.chat = c) = :size " +
            "AND c.id IN (SELECT cp.chat.id FROM ChatParticipant cp WHERE cp.user.id IN :participantIds)")
    List<Chat> findChatsByExactParticipantIds(@Param("participantIds") List<Long> participantIds, @Param("size") int size);

    @Query(value = "SELECT c.id " +
            "FROM chats c " +
            "JOIN chat_participants cp ON c.id = cp.chat_id " +
            "WHERE cp.user_id IN (:participantIds) " +
            "GROUP BY c.id " +
            "HAVING COUNT(DISTINCT cp.user_id) = :size " +
            "AND COUNT(DISTINCT cp.user_id) = " +
            "(SELECT COUNT(*) FROM chat_participants WHERE chat_id = c.id)",
            nativeQuery = true)
    List<Long> findChatIdsByExactParticipants(
            @Param("participantIds") List<Long> participantIds,
            @Param("size") int size
    );




}
