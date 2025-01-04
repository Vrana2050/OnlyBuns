package rs.ac.uns.ftn.onlybunsapp.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.Message;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.ChatRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.MessageRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.ChatService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;


    public Message sendMessage(MessageDto messageDto) {
        // Verify chat exists and is accessible to sender
        Chat chat = chatRepository.findById(messageDto.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with ID: " + messageDto.getChatId()));

        User sender = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + messageDto.getSenderId()));

        // Verify sender is a participant
        if (!chat.getParticipants().contains(sender)) {
            throw new AccessDeniedException("User is not a participant in this chat");
        }

        Message message = new Message();
        message.setContent(messageDto.getContent());
        message.setSender(sender);
        message.setChat(chat);
        message.setCreatedAt(Timestamp.from(Instant.now()));
        message.setRead(false);

        return messageRepository.save(message);
    }


    public List<Message> getChatHistory(Long chatId) {
        return messageRepository.findByChatIdOrderByCreatedAtDesc(chatId);
    }

    public Chat createOrGetPrivateChat(Long user1Id, Long user2Id) {
        // First check if users exist
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + user1Id));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + user2Id));

        // Check if a private chat already exists between these users
        Chat existingChat = chatRepository.findPrivateChatBetweenUsers(user1Id, user2Id);
        if (existingChat != null) {
            return existingChat;
        }

        // Create new private chat if none exists
        Chat newChat = new Chat();
        // Sort usernames alphabetically to ensure consistent naming regardless of who initiates
        String chatName = Stream.of(user1.getUsername(), user2.getUsername())
                .sorted()
                .collect(Collectors.joining(" - "));

        newChat.setName(chatName);
        newChat.setPrivate(true);
        newChat.setCreatedAt(Timestamp.from(Instant.now()));
        newChat.setAdmin(user1); // Consider if you really need an admin for private chats

        // Add participants
        newChat.getParticipants().add(user1);
        newChat.getParticipants().add(user2);

        return chatRepository.save(newChat);
    }


    public List<Chat> getUserChats(Long userId) {
        return chatRepository.findUserChats(userId);
    }


    public Chat findChatById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with ID: " + chatId));
    }

    public List<Message> getChatHistoryBetweenUsers(Long chatId) {
        // Logic to fetch messages between two users
        return messageRepository.findByChatIdOrderByCreatedAtDesc(chatId);
    }

}
