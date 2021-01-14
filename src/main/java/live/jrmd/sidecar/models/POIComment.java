package live.jrmd.sidecar.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "poi_Comments")
public class POIComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, columnDefinition = "DATE")
    private Date date;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "poi_id")
    private POI poi;

    public POIComment(){}

    //write
    public POIComment(String comment, Date date, User user, POI poi) {
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.poi = poi;
    }

    //read
    public POIComment(Long id, String comment, Date date, User user, POI poi) {
        this.id = id;
        this.comment = comment;
        this.date = date;
        this.user = user;
        this.poi = poi;
    }

    //copy
    public POIComment(POIComment copy) {
        id = copy.id;
        comment = copy.comment;
        date = copy.date;
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

    public POI getPoi() {
        return poi;
    }

    public void setPoi(POI poi) {
        this.poi = poi;
    }


    public String getFormattedDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(date);
    }

}
