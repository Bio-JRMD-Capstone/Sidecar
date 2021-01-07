package live.jrmd.sidecar.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "e_categories")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "eventCategories", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Event> events;

    public EventCategory(){}

    public EventCategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public EventCategory(String name) {
        this.name = name;
    }

    public EventCategory(EventCategory copy){
        this.id = copy.id;
        this.name = copy.name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
