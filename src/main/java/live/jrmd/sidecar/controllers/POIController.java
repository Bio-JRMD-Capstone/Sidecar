package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.POIRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "points/index";
    }
    @GetMapping("/searchPOIs")
    public String search(@RequestParam(name = "term") String term, Model model){
        term = "%"+term+"%";
        List searchPOIs = poiDao.findAllByNameIsLike(term);
        model.addAttribute("points", searchPOIs);
        return "points/index";
    }
    @GetMapping("/points.json")
    public @ResponseBody List<POI> viewAllPOIInJSONFormat() {
        return poiDao.findAll();
    }
    @GetMapping("/points/add")
    public String add(Model model) {
        POI newPoi = new POI();
        model.addAttribute("poi", newPoi);
        return "points/add";
    }
    @PostMapping("/points/add")
    public String create(@ModelAttribute POI poiToBeSaved) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        poiToBeSaved.setUser(userDb);
        POI dbPOI = poiDao.save(poiToBeSaved);
        return "redirect:/points";
    }
}
