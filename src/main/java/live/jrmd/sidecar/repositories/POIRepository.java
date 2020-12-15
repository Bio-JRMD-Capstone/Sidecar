package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface POIRepository extends JpaRepository<POI, Long> {
    List<POI> findAllByNameIsLike(String term);

    Object findAllByUser(User user);
}
