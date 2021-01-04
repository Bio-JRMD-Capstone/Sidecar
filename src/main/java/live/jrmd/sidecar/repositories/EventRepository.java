package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.Event;
import live.jrmd.sidecar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByNameIsLike(String term);
    Event getEventById(Long id);
    List<Event> findAllByUser(User user);
}
