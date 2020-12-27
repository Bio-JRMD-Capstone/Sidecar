package live.jrmd.sidecar.repositories;

import live.jrmd.sidecar.models.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventCatRepository extends JpaRepository<EventCategory, Long> {
    List<EventCategory> findAll();
}
