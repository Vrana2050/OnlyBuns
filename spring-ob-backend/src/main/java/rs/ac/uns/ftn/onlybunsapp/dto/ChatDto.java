package rs.ac.uns.ftn.onlybunsapp.dto;

import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;

import java.util.Set;

public class ChatDto {
    private Long id;
    private String name;
    private UserReadDto admin;
    private Set<UserReadDto> participants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserReadDto getAdmin() {
        return admin;
    }

    public void setAdmin(UserReadDto admin) {
        this.admin = admin;
    }

    public Set<UserReadDto> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UserReadDto> participants) {
        this.participants = participants;
    }
}
