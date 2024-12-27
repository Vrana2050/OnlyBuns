package rs.ac.uns.ftn.onlybunsapp.service;

import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentCreateDto;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;
import rs.ac.uns.ftn.onlybunsapp.model.User;

import java.nio.file.AccessDeniedException;

public interface CommentService{
    public Comment CreateComment(User creator, CommentCreateDto commentDto)throws AccessDeniedException;
}
