package rs.ac.uns.ftn.onlybunsapp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.onlybunsapp.model.Location;
import rs.ac.uns.ftn.onlybunsapp.repository.LocationRepository;
import rs.ac.uns.ftn.onlybunsapp.service.LocationService;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Override
    public Location cacheLocation(Location location) {
        System.out.println("Kao gas");
        return location;
    }

    @Override
    public List<Location> getAll() {
        return locationRepository.findAll();
    }

    @Override
    public Location getById(long id) {
        return locationRepository.findById(id);
    }
}
