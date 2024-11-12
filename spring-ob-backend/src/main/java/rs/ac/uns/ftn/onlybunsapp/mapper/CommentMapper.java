package rs.ac.uns.ftn.onlybunsapp.mapper;

import org.mapstruct.Mapper;
import rs.ac.uns.ftn.onlybunsapp.dto.commentDtos.CommentReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.Comment;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    public CommentReadDto toCommentReadDto(Comment comment);
}
