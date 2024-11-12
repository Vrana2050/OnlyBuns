package rs.ac.uns.ftn.onlybunsapp.dto.postDtos;

import javax.validation.constraints.NotNull;

public class LocationDto {
    public Long id;
    @NotNull( message = "Latitude is required")
    public double latitude;
    @NotNull( message = "Longitude is required")
    public double longitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
