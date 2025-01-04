package rs.ac.uns.ftn.onlybunsapp.service;

import rs.ac.uns.ftn.onlybunsapp.dto.ChatDto;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.Message;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

public interface ChatService {
    public Message sendMessage(MessageDto messageDto);
    public List<Message> getChatHistory(Long chatId);
    public Chat createOrGetPrivateChat(Long user1Id, Long user2Id);
    public List<Chat> getUserChats(Long userId);
}
