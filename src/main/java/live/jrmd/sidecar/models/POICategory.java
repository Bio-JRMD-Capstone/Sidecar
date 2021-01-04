package live.jrmd.sidecar.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "p_categories")
public class POICategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "pCategoryList")
    private List<POI> poiList;

    public POICategory(){}

    public POICategory(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public POICategory(String name) {
        this.name = name;
    }

    public POICategory(POICategory copy){
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

    public List<POI> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<POI> poiList) {
        this.poiList = poiList;
    }
}
