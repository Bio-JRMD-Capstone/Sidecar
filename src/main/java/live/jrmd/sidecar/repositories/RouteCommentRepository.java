package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.Route;
import live.jrmd.sidecar.models.RouteComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteCommentRepository extends JpaRepository<RouteComment, Long> {
    List<RouteComment> findAllByRoute(Route route);
}
