package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.POICatRepository;
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
    private final POICatRepository pCatDao;

    public POIController(POIRepository poiDao, UserRepository userDao, POICatRepository pCatDao) {
        this.poiDao = poiDao;
        this.userDao = userDao;
        this.pCatDao = pCatDao;
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
    @GetMapping("/points/create")
    public String addPOIs(Model model) {
        model.addAttribute("poi", new POI());
        model.addAttribute("pCategories", pCatDao.findAll());
        return "points/create";
    }
    @PostMapping("/points/create")
    public String create(@ModelAttribute POI poiToBeSaved) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        poiToBeSaved.setUser(userDb);
        POI dbPOI = poiDao.save(poiToBeSaved);
        return "redirect:/points";
    }
    @PostMapping("/point/{id}/delete")
    public String deleteRoute (@PathVariable(value = "id") long id) {
        POI poiToDelete = poiDao.getPOIById(id);
        poiDao.delete(poiToDelete);
        return "redirect:/profile";
    }
}
