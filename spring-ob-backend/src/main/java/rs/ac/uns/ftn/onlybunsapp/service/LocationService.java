package rs.ac.uns.ftn.onlybunsapp.service;

import org.springframework.cache.annotation.Cacheable;
import rs.ac.uns.ftn.onlybunsapp.model.Location;

import java.util.List;

public interface LocationService {
    @Cacheable(value = "location", key = "#location.id")
    public Location cacheLocation(Location location);

    @Cacheable(value = "location", key = "#id")
    public List<Location> getAll();
    public Location getById(long id);
}
