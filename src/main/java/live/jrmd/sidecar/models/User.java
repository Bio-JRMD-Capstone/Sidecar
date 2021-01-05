package live.jrmd.sidecar.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 24, unique = true)
    private String username;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String password_confirm;

    @Column(length = 10)
    private String zipcode;

    @Column()
    private String photo_url;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonBackReference
    private List<POI> poiList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Event> events;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private List<Route> routes;

    public User(){}
    //read
    public User(Long id, String username, String email, String password, String password_confirm, String zipcode, String photo_url) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.password_confirm = password_confirm;
        this.zipcode = zipcode;
        this.photo_url = photo_url;
    }
    //create
    public User(String username, String email, String password, String password_confirm, String zipcode, String photo_url) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.password_confirm = password_confirm;
        this.zipcode = zipcode;
        this.photo_url = photo_url;
    }
    //copy
    public User (User copy){
        id = copy.id;
        username = copy.username;
        email = copy.email;
        password = copy.password;
        password_confirm = copy.password_confirm;
        zipcode = copy.zipcode;
        photo_url = copy.photo_url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<POI> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<POI> poiList) {
        this.poiList = poiList;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}