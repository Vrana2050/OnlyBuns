package rs.ac.uns.ftn.onlybunsapp.service;

import rs.ac.uns.ftn.onlybunsapp.dto.ChatDto;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatInboxDto;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.Message;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.util.List;

public interface ChatService {
    public Message sendMessage(MessageDto messageDto);
    public List<MessageDto> getChatHistory(Long chatId, Long userId);
    public ChatDto createOrGetPrivateChat(Long user1Id, Long user2Id);
    public List<ChatInboxDto> getUserChats(Long userId);
    public ChatDto findChatById(Long chatId);
    public ChatDto removeMember(Long chatId, Long removeUserId,Long requestingUserId) throws IllegalAccessException ;
    public ChatDto addMember(Long chatId, Long newUserId, Long userId);
    public ChatDto createChatIfNotExists(List<Long> participantIds, Long userId) ;

}
