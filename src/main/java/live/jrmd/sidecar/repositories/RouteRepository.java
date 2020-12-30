package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.Route;
import live.jrmd.sidecar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findAllByTitleIsLike(String term);
    List<Route> findAllByUser(User user);
}
