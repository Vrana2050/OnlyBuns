package rs.ac.uns.ftn.onlybunsapp.mapper;

import org.mapstruct.Mapper;
import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.LocationDto;
import rs.ac.uns.ftn.onlybunsapp.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toLocationDomain(LocationDto locationDto);

    LocationDto toLocationDto(Location location);
}
