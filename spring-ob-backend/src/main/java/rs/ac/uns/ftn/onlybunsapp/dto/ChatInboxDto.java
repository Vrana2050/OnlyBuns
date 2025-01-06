package rs.ac.uns.ftn.onlybunsapp.dto;

import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;

import java.util.Set;

public class ChatInboxDto {
    private Long chatId;
    private MessageDto lastMessage;
    private Set<UserReadDto> participants;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Set<UserReadDto> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserReadDto> participants) {
        this.participants = participants;
    }

    public MessageDto getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageDto lastMessage) {
        this.lastMessage = lastMessage;
    }
}
