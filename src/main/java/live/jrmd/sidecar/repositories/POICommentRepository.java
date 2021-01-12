package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.POIComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface POICommentRepository extends JpaRepository<POIComment, Long> {
        List<POIComment> findAllByPoiId(Long id);
}
