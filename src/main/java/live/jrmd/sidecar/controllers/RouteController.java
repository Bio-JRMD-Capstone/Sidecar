package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.repositories.RouteRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RouteController {
    private final RouteRepository routeDao;
    private final UserRepository userDao;

    public RouteController(RouteRepository routeDao, UserRepository userDao){
        this.routeDao = routeDao;
        this.userDao = userDao;
    }

    @GetMapping("/routes/create")
    public String buildARoute(){
        return "routes/create";
    }

    @GetMapping("/routes")
    public String showAllRoutes(Model model){
        model.addAttribute("routes", routeDao.findAll());
        return "routes/index";
    }

    @GetMapping("/searchRoutes")
    public String search(@RequestParam(name = "term") String term, Model model){
        term = "%"+term+"%";
        List searchRoutes = routeDao.findAllByTitleIsLike(term);
        model.addAttribute("routes", searchRoutes);
        return "routes/index";
    }
}
