package rs.ac.uns.ftn.onlybunsapp.dto;


import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.LocationDto;

public class MessageDto {

    private long id;
    private String name;
    private LocationDto location;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }
}
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
