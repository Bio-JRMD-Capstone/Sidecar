package live.jrmd.sidecar.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String date;

    @Column(length = 10, nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String eventType;

    @ManyToOne
    @JoinColumn()
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "events_categories",
        joinColumns = {@JoinColumn(name = "event_id")},
        inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<EventCategory> eventCategories;

    public Event(){}

    //read
    public Event(Long id, String name, String description, String date, String zipcode, String eventType, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.zipcode = zipcode;
        this.eventType = eventType;
        this.user = user;
    }
    //create
    public Event(String name, String description, String date, String zipcode, String eventType) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.zipcode = zipcode;
        this.eventType = eventType;
    }
    public Event (Event copy){
        this.name = copy.name;
        this.description = copy.description;
        this.date = copy.date;
        this.zipcode = copy.zipcode;
        this.eventType = copy.eventType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public List<EventCategory> getEventCategories() {
        return eventCategories;
    }

    public void setEventCategories(List<EventCategory> eventCategories) {
        this.eventCategories = eventCategories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
