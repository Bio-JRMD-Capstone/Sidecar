package live.jrmd.sidecar.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
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

    @Column(nullable = true, columnDefinition = "TEXT")
    private String filePath;

    @Column(nullable = false)
    private String lat;

    @Column(nullable = false)
    private String lon;

    @ManyToOne
    @JoinColumn()
    private User user;

    @ManyToMany
    @JoinTable(
        name = "events_categories",
        joinColumns = {@JoinColumn(name = "event_id")},
        inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    private List<EventCategory> eventCategories;

    public Event(){}
///
    //read
    public Event(Long id, String lat, String lon, String name, String description, String date, String zipcode, String filePath, User user, List<EventCategory> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.zipcode = zipcode;
        this.filePath = filePath;
        this.user = user;
        this.eventCategories = categories;
        this.lat = lat;
        this.lon = lon;
    }

    //create
    public Event(String name, String lat, String lon, String description, String date, String zipcode, String filePath, List<EventCategory> categories) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.zipcode = zipcode;
        this.filePath = filePath;
        this.eventCategories = categories;
        this.lat = lat;
        this.lon = lon;
    }

    public Event(Event copy, String filePath){
        this.name = copy.name;
        this.description = copy.description;
        this.date = copy.date;
        this.zipcode = copy.zipcode;
        this.filePath = filePath;
        lat = copy.lat;
        lon = copy.lon;
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

    public List<EventCategory> getEventCategories() { return eventCategories; }

    public void setEventCategories(List<EventCategory> eventCategories) { this.eventCategories = eventCategories; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
