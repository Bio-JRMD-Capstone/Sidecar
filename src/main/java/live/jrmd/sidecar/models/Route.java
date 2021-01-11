package live.jrmd.sidecar.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.security.PrivateKey;
import java.util.List;

@Entity
@Table(name="routes")
public class Route {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Routes must have a title")
    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 255)
    private String distance;

    @Column(nullable = false, length = 25)
    private String time;

    @NotBlank(message = "Routes must have a description")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Routes must have markers on the map")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String coordinates;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "route")
    private List<RouteComment> routeComments;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Route() {}

    //write
    public Route(String title, String distance, String time, String description, String coordinates) {
        this.title = title;
        this.distance = distance;
        this.time = time;
        this.description = description;
        this.coordinates = coordinates;
    }

    //read
    public Route(long id, String title, String distance, String time, String description, String coordinates) {
        this.id = id;
        this.title = title;
        this.distance = distance;
        this.time = time;
        this.description = description;
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", distance='" + distance + '\'' +
                ", time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", coordinates='" + coordinates + '\'' +
                ", routeComments=" + routeComments +
                ", user=" + user +
                '}';
    }

    public long getId () {
        return this.id;
    }

    public void setId(long newId) {
        this.id = newId;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String newTitle) {
        this.title = newTitle;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setDistance(String newDistance) {
        this.distance = newDistance;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public String getCoordinates() {
        return this.coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public List<RouteComment> getRouteComments() {
        return routeComments;
    }

    public void setRouteComments(List<RouteComment> routeComments) {
        this.routeComments = routeComments;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
