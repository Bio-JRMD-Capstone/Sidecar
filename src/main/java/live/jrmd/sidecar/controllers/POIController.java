package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.POIRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class POIController {
    private final POIRepository poiDao;
    private final UserRepository userDao;

    public POIController(POIRepository poiDao, UserRepository userDao) {
        this.poiDao = poiDao;
        this.userDao = userDao;
    }

    @GetMapping("/points")
    public String showAllPOIs(Model model){
        model.addAttribute("points", poiDao.findAll());
        return "points/index";
    }
    @GetMapping("/searchPOIs")
    public String search(@RequestParam(name = "term") String term, Model model){
        term = "%"+term+"%";
        List searchPOIs = poiDao.findAllByNameIsLike(term);
        model.addAttribute("points", searchPOIs);
        return "points/index";
    }
    @GetMapping("/points/add")
    public String add(Model model) {
        model.addAttribute("poi", new POI());
        model.addAttribute("points", poiDao.findAll());
        return "points/add";
    }
    @PostMapping("/points/add")
    public String create(@ModelAttribute POI poiToBeSaved) {
        User user = userDao.getOne(1L);
        poiToBeSaved.setUser(user);
        POI dbPOI = poiDao.save(poiToBeSaved);
        return "redirect:/points";
    }
}
