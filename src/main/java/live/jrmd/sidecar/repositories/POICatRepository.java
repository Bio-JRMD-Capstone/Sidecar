package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.POICategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface POICatRepository extends JpaRepository<POICategory, Long> {
    List<POICategory> findAll();
    List<POICategory> findAllByNameIsLike(String term);

}
