package rs.ac.uns.ftn.onlybunsapp.dto.commentDtos;

import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;

import java.sql.Timestamp;

public class CommentReadDto
{
    public long id;
    public String text;
    public UserReadDto creator;
    public Timestamp created;
}
