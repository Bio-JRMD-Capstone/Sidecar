package live.jrmd.sidecar.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="route_comments")
public class RouteComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, columnDefinition = "DATE")
    private Date date;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn (name = "user_id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn (name = "route_id")
    private Route route;

    public RouteComment() {}

    //write
    public RouteComment(String comment, Date date, User user, Route route) {
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.route = route;
    }

    //read
    public RouteComment(long id, String comment, Date date, User user, Route route) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.route = route;
    }

    //Copy
    public RouteComment(RouteComment copy) {
        id = copy.id;
        comment = copy.comment;
        date = copy.date;
        user = copy.user;
        route = copy.route;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }
}
