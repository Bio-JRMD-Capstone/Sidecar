package live.jrmd.sidecar.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "POIS")
public class POI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lat;

    @Column(nullable = false)
    private String lon;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String photo_url;

    @Column
    private String category;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "poi")
    @JsonBackReference
    private List<POIComment> poiComments;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="pois_categories",
            joinColumns={@JoinColumn(name="poi_id")},
            inverseJoinColumns={@JoinColumn(name="category_id")}
    )
    private List<POICategory> pCategoryList;



    public POI(){}

    //read
    public POI(Long id, String name, String lat, String lon, String description, User user, String photo_url) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.description = description;
        this.photo_url = photo_url;
        this.user = user;
    }

    //create
    public POI(String name, String lat, String lon, String description, String photo_url) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.description = description;
        this.photo_url = photo_url;
    }

    //copy
    public POI(POI copy) {
        this.id = copy.id;
        name = copy.name;
        lat = copy.lat;
        lon = copy.lon;
        description = copy.description;
        photo_url = copy.photo_url;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<POICategory> getpCategoryList() {
        return pCategoryList;
    }

    public void setpCategoryList(List<POICategory> pCategoryList) {
        this.pCategoryList = pCategoryList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<POIComment> getPoiComments() {
        return poiComments;
    }

    public void setPoiComments(List<POIComment> poiComments) {
        this.poiComments = poiComments;
    }
}
