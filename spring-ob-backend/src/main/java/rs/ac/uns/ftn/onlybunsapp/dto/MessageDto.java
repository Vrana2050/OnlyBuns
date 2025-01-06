package rs.ac.uns.ftn.onlybunsapp.dto;

import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;

import java.sql.Timestamp;

public class MessageDto {
    private String content;
    private ChatDto chat;
    private UserReadDto sender;
    private Timestamp createdAt;

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public MessageDto() {
    }


    public UserReadDto getSender() {
        return sender;
    }

    public void setSender(UserReadDto sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatDto getChat() {
        return chat;
    }

    public void setChat(ChatDto chat) {
        this.chat = chat;
    }

}
