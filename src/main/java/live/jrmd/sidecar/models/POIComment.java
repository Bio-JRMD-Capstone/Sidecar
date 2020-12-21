package live.jrmd.sidecar.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "poi_Comments")
public class POIComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private String timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "poi_id")
    private POI poi;

    public POIComment(){}

    //create
    public POIComment(String comment, String timestamp) {
        this.comment = comment;
        this.timestamp = timestamp;
    }

    //read
    public POIComment(Long id, String comment, String timestamp, User user, POI poi) {
        this.id = id;
        this.comment = comment;
        this.timestamp = timestamp;
        this.user = user;
        this.poi = poi;
    }

    //copy
    public POIComment(POIComment copy) {
        id = copy.id;
        comment = copy.comment;
        timestamp = copy.timestamp;
        user = copy.user;
        poi = copy.poi;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }
}
