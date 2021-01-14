package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.Route;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CombinedController {

    private final EventRepository eventDao;
    private final EventCatRepository eCatDao;
    private final POIRepository poiDao;
    private final POICatRepository pCatDao;
    private final RouteRepository routeDao;
    private final UserRepository userDao;
    private final UsersRepository users;

    public CombinedController(EventRepository eventDao, EventCatRepository eCatDao, POIRepository poiDao, POICatRepository pCatDao, RouteRepository routeDao, UserRepository userDao, UsersRepository users) {
        this.eventDao = eventDao;
        this.eCatDao = eCatDao;
        this.poiDao = poiDao;
        this.pCatDao = pCatDao;
        this.routeDao = routeDao;
        this.userDao = userDao;
        this.users = users;
    }

    @GetMapping("/all")
    public String showCombined(Model model){
        try {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("location", userDb.getZipcode());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        model.addAttribute("routes", routeDao.findAll());
        model.addAttribute("points", poiDao.findAll());
        return "combined/index";
    }

    @GetMapping("/all/search")
    public String showCombined(Model model, @RequestParam(name = "entered-location") String enteredLocation){
        model.addAttribute("location", enteredLocation);
        model.addAttribute("routes", routeDao.findAll());
        model.addAttribute("points", poiDao.findAll());
        return "combined/index";
    }
}

