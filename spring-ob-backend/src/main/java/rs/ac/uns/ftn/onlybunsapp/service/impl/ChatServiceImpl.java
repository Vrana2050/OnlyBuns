package rs.ac.uns.ftn.onlybunsapp.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatDto;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatInboxDto;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;
import rs.ac.uns.ftn.onlybunsapp.mapper.MessageMapper;
import rs.ac.uns.ftn.onlybunsapp.mapper.UserMapper;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.ChatParticipant;
import rs.ac.uns.ftn.onlybunsapp.model.Message;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.ChatParticipantRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.ChatRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.MessageRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.ChatService;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
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

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChatParticipantRepository chatParticipantRepository;


    public Message sendMessage(MessageDto messageDto) {
        // Verify chat exists and is accessible to sender
        Chat chat = chatRepository.findById(messageDto.getChat().getId())
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with ID: " + messageDto.getChat().getId()));

        User sender = userRepository.findById(messageDto.getSender().getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + messageDto.getSender().getId()));

        // Verify sender is a participant
        if (!chat.getParticipants().stream()
                .anyMatch(participant -> participant.getUser().getId().equals(sender.getId()))) {
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


    public List<MessageDto> getChatHistory(Long chatId, Long userId) {
        // Fetch the chat participant to get the joinedAt timestamp
        ChatParticipant cp = chatParticipantRepository.findByChatIdAndUserId(chatId, userId);
        if (cp == null) {
            throw new IllegalArgumentException("User is not a participant of this chat.");
        }

        Timestamp joinedAt = cp.getJoinedAt();

        // Fetch the 10 most recent messages before the user joined the chat
        List<Message> messagesBefore = messageRepository.findTop10ByChatIdAndCreatedAtLessThanOrderByCreatedAtDesc(
                chatId,
                joinedAt
        );

        // Fetch all messages after the user joined the chat
        List<Message> messagesAfter = messageRepository.findByChatIdAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
                chatId,
                joinedAt
        );

        // Combine the messages
        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messagesBefore);
        allMessages.addAll(messagesAfter);

        // Map to DTOs and return
        return messageMapper.toDtoList(allMessages);
    }


    public ChatDto createOrGetPrivateChat(Long user1Id, Long user2Id) {
        // First check if users exist
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + user1Id));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + user2Id));

        // Check if a private chat already exists between these users
        Chat existingChat = chatRepository.findPrivateChatBetweenUsers(user1Id, user2Id);
        if (existingChat != null) {
            return mapToDto(existingChat);
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

        //not setting a admin for now

        // Add participants
        ChatParticipant participant1 = new ChatParticipant(newChat, user1);
        ChatParticipant participant2 = new ChatParticipant(newChat, user2);


        newChat.getParticipants().add(participant1);
        newChat.getParticipants().add(participant2);

        Chat chat = chatRepository.save(newChat);
        return mapToDto(chat);
    }


    public List<ChatInboxDto> getUserChats(Long userId) {
        List<Chat> chats = chatParticipantRepository.findDistinctChatsByUserId(userId);
        List<ChatInboxDto> chatDtos = new ArrayList<ChatInboxDto>();
        for(Chat chat : chats){
            ChatInboxDto chatInboxDto = new ChatInboxDto();
            chatInboxDto.setChatId(chat.getId());
            chatInboxDto.setParticipants(chat.getParticipants().stream().map(participant -> userMapper.toUserReadDto(participant.getUser())).collect(Collectors.toSet()));
            Message lastMessage = messageRepository.findTop1ByChatId(chat.getId(), PageRequest.of(0, 1))
                    .stream()
                    .findFirst()
                    .orElse(null);
            chatInboxDto.setLastMessage(messageMapper.toMessageDto(lastMessage));
            chatDtos.add(chatInboxDto);
        }
        return chatDtos;
    }


    public ChatDto findChatById(Long chatId) {
        return mapToDto(chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat not found with ID: " + chatId)));
    }


    public ChatDto addMember(Long chatId, Long newUserId, Long userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));

        User newUser = userRepository.findById(newUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ChatParticipant newParticipant = new ChatParticipant(chat, newUser);
        // Check if user is already a member
        if (chat.getParticipants().contains(newParticipant)) {
            throw new IllegalArgumentException("User is already a member of this chat");
        }

        // Add the new member
        chat.getParticipants().add(newParticipant);

        // Set the requesting user as the admin
        User requestingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Requesting user not found"));
        chat.setAdmin(requestingUser);

        Chat updatedChat = chatRepository.save(chat);
        return mapToDto(updatedChat);
    }

    public ChatDto removeMember(Long chatId, Long userId, Long requesterId) throws IllegalAccessException {
        try {
            Chat chat = chatRepository.findById(chatId)
                    .orElseThrow(() -> {
                        System.out.println("Chat not found with ID: " + chatId);
                        return new IllegalArgumentException("Chat not found");
                    });

            // Check if requester is admin
            if (!chat.getAdmin().getId().equals(requesterId)) {
                System.out.println("User " + requesterId + " is not admin of chat " + chatId);
                throw new IllegalAccessException("Only chat admin can remove members");
            }

            // Check if user exists in chat
            ChatParticipant participantToRemove = chat.getParticipants().stream()
                    .filter(p -> {
                        System.out.println("Checking participant: User ID = " + p.getUser().getId());
                        return p.getUser().getId().equals(userId);
                    })
                    .findFirst()
                    .orElseThrow(() -> {
                        System.out.println("User " + userId + " not found in chat " + chatId);
                        return new IllegalArgumentException("User not found in chat");
                    });

            System.out.println("Found participant to remove: " + participantToRemove.getId());
            chat.getParticipants().remove(participantToRemove);
            chatParticipantRepository.delete(participantToRemove);

            Chat updatedChat = chatRepository.save(chat);
            return mapToDto(updatedChat);
        } catch (Exception e) {
            System.out.println("Error in removeMember: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional
    public ChatDto createChatIfNotExists(List<Long> participantIds, Long userId) {
        List<Long> sortedIds = new ArrayList<>(participantIds);
        Collections.sort(sortedIds);

        List<Chat> potentialChats = chatRepository.findChatsByParticipantIds(participantIds);

        // Check if any of these chats have EXACTLY these participants (no more, no less)
        for (Chat chat : potentialChats) {
            List<Long> chatParticipantIds = chat.getParticipants().stream()
                    .map(participant -> participant.getUser().getId())
                    .sorted()
                    .collect(Collectors.toList());

            if (chatParticipantIds.equals(sortedIds)) {
                return mapToDto(chat); // Found existing chat with exact participants
            }
        }

        Chat newChat = new Chat();
        newChat.setName("Group Chat"); // Or generate based on participants
        newChat.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        // Set participants
        List<User> participants = userRepository.findAllById(participantIds);


        newChat.setParticipants(new HashSet<>(participants.stream().map(participant -> new ChatParticipant(newChat, participant)).collect(Collectors.toSet())));
        User admin = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        newChat.setAdmin(admin);

        return mapToDto(chatRepository.save(newChat));
    }



    public static ChatDto mapToDto(Chat chat) {
        ChatDto chatDto = new ChatDto();

        chatDto.setId(chat.getId());
        chatDto.setName(chat.getName());

        // Map admin to UserReadDto
        if (chat.getAdmin() != null) {
            UserReadDto adminDto = new UserReadDto();
            adminDto.setId(chat.getAdmin().getId());
            adminDto.setUsername(chat.getAdmin().getUsername());
            chatDto.setAdmin(adminDto);
        }

        // Map participants to a set of UserReadDto
        chatDto.setParticipants(chat.getParticipants().stream()
                .map(participant -> {
                    User user = participant.getUser();
                    UserReadDto userDto = new UserReadDto();
                    userDto.setId(user.getId());
                    userDto.setUsername(user.getUsername());
                    return userDto;
                })
                .collect(Collectors.toSet()));

        return chatDto;
    }
}
