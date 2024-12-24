package rs.ac.uns.ftn.onlybunsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.onlybunsapp.model.Location;
import rs.ac.uns.ftn.onlybunsapp.model.PostUserLike;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
