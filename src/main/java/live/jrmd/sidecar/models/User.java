package live.jrmd.sidecar.models;


import javax.persistence.*;

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

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 10)
    private String zipcode;

    @Column(length = 255)
    private String photo_url;

    public User(){}
    //read
    public User(Long id, String username, String email, String password, String zipcode, String photo_url) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.zipcode = zipcode;
        this.photo_url = photo_url;
    }
    //create
    public User(String username, String email, String password, String zipcode, String photo_url) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.zipcode = zipcode;
        this.photo_url = photo_url;
    }
    //copy
    public User (User copy){
        id = copy.id;
        username = copy.username;
        email = copy.email;
        password = copy.password;
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
}