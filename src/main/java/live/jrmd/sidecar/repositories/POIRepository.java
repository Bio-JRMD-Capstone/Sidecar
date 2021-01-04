package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.Route;
import live.jrmd.sidecar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface POIRepository extends JpaRepository<POI, Long> {
    List<POI> findAllByNameIsLike(String term);

    List<POI> findAllByUser(User user);

    POI getPOIById(long id);
}
