package rs.ac.uns.ftn.onlybunsapp.mapper;


import org.mapstruct.Mapper;
import rs.ac.uns.ftn.onlybunsapp.dto.ChatDto;
import rs.ac.uns.ftn.onlybunsapp.dto.MessageDto;
import rs.ac.uns.ftn.onlybunsapp.model.Chat;
import rs.ac.uns.ftn.onlybunsapp.model.Message;

import java.util.List;


@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface MessageMapper {
    public MessageDto toMessageDto(Message message);
    public List<MessageDto> toDtoList(List<Message> messages);

}
