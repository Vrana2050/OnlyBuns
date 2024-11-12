package rs.ac.uns.ftn.onlybunsapp.mapper;

import org.mapstruct.Mapper;
import org.springframework.context.annotation.Bean;
import rs.ac.uns.ftn.onlybunsapp.dto.userDtos.UserReadDto;
import rs.ac.uns.ftn.onlybunsapp.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserReadDto toUserReadDto(User creator);
}
