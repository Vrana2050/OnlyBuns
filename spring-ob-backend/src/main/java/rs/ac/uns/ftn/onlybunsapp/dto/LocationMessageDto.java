package rs.ac.uns.ftn.onlybunsapp.dto;

import rs.ac.uns.ftn.onlybunsapp.dto.postDtos.LocationDto;

public class LocationMessageDto {

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
