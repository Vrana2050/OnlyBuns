package rs.ac.uns.ftn.onlybunsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
        System.out.println("Received message: " + message);
        Message savedMessage = chatService.sendMessage(message);

        simpMessagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), savedMessage);
    }


    // REST endpoint to fetch messages for a specific chat
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<Message>> getMessagesByChatId(@PathVariable Long chatId) {
        System.out.println("Fetching messages for chat: " + chatId);
        List<Message> messages = chatService.getChatHistory(chatId);
        return ResponseEntity.ok(messages);
    }

    // REST endpoint to fetch or create a chat between two users
    @PostMapping("/create-or-get")
    public ResponseEntity<Chat> getOrCreateChat(@RequestBody List<Long> participantIds) {
        if (participantIds.size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println(participantIds);

        Long userId1 = participantIds.get(0);
        Long userId2 = participantIds.get(1);

        // Create the chat if both users are found
        Chat chat = chatService.createOrGetPrivateChat(userId1, userId2);

        return ResponseEntity.ok(chat);
    }

    // REST endpoint to get all chats for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Chat>> getChatsForUser(@PathVariable Long userId) {
        List<Chat> chats = chatService.getUserChats(userId);
        return ResponseEntity.ok(chats);
    }
}