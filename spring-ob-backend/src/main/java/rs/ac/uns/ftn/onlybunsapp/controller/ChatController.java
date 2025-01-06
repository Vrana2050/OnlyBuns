package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatDto;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatInboxDto;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.Message;
import rs.ac.uns.ftn.onlybunsapp.model.User;
import rs.ac.uns.ftn.onlybunsapp.repository.MessageRepository;
import rs.ac.uns.ftn.onlybunsapp.repository.UserRepository;
import rs.ac.uns.ftn.onlybunsapp.service.ChatService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    // WebSocket endpoint for sending messages
    @MessageMapping("/send")
    public void sendMessage(@Payload MessageDto message) {

        if (chatService == null) {
            System.out.println("ChatService is null");
        }

        Message savedMessage = chatService.sendMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + message.getChat().getId(), savedMessage);
    }


    // REST endpoint to fetch messages for a specific chat
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDto>> getMessagesByChatId(@PathVariable Long chatId, @AuthenticationPrincipal User user) {
        System.out.println("Fetching messages for chat: " + chatId);
        List<MessageDto> messages = chatService.getChatHistory(chatId, user.getId());
        System.out.println("Messages: " + messages);
        return ResponseEntity.ok(messages);
    }

    // REST endpoint to fetch or create a chat between two users
    @PostMapping("/create-or-get")
    public ResponseEntity<ChatDto> getOrCreateChat(@RequestBody List<Long> participantIds) {
        if (participantIds.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println(participantIds);

        Long userId1 = participantIds.get(0);
        Long userId2 = participantIds.get(1);

        // Create the chat if both users are found
        ChatDto chat = chatService.createOrGetPrivateChat(userId1, userId2);

        return ResponseEntity.ok(chat);
    }

    // REST endpoint to get all chats for a specific user
    @GetMapping("/forUser")
    public ResponseEntity<List<ChatInboxDto>> getChatsForUser(@AuthenticationPrincipal User user) {
        List<ChatInboxDto> chats = chatService.getUserChats(user.getId());
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> getChatById(@PathVariable Long chatId) {
        ChatDto chat = chatService.findChatById(chatId);
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/{chatId}/members/{userId}")
    public ResponseEntity<ChatDto> addMember(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal User user) {
        ChatDto updatedChat = chatService.addMember(chatId, userId,user.getId());
        return ResponseEntity.ok(updatedChat);
    }

    @DeleteMapping("/{chatId}/members/{userId}")
    public ResponseEntity<ChatDto> removeMember(@PathVariable Long chatId, @PathVariable Long userId, @AuthenticationPrincipal User user) {
        try {
            ChatDto updatedChat = chatService.removeMember(chatId, userId, user.getId());
            return ResponseEntity.ok(updatedChat);
        } catch (IllegalAccessException e) {
            System.out.println("Access error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (IllegalArgumentException e) {
            System.out.println("Argument error: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/newChat")
    public ResponseEntity<ChatDto> createChat(@RequestBody List<Long> participantIds,@AuthenticationPrincipal User user) {
        ChatDto chat = chatService.createChatIfNotExists(participantIds,user.getId());
        participantIds.forEach(participantId -> {
            simpMessagingTemplate.convertAndSend("/topic/new-chat/" + participantId, chat);
        });
        return ResponseEntity.ok(chat);
    }


}