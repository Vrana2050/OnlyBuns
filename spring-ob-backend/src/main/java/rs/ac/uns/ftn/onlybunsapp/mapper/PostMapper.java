package rs.ac.uns.ftn.onlybunsapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.LocationDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostCreateDto;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.PostReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.Location;
import rs.ac.uns.ftn.onlybunsapp.model.Post;

@Mapper(componentModel = "spring", uses = {LocationMapper.class, UserMapper.class, CommentMapper.class})
public interface PostMapper {
    Post toPostDomain(PostCreateDto postDto);

    PostReadDto toPostReadDto(Post post);
}

