package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.POI;
import live.jrmd.sidecar.models.Route;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.RouteRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String buildARoute(Model model){
        model.addAttribute("route", new Route());
        return "routes/create";
    }

    @PostMapping("/routes/create")
    public String saveRoute(@ModelAttribute Route route){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        route.setUser(user);
        routeDao.save(route);
        return "redirect:/routes";
    }

    @GetMapping("/routes")
    public String showAllRoutes(Model model){
        model.addAttribute("routes", routeDao.findAll());
        return "routes/index";
    }

    @GetMapping("/routes.json")
    public @ResponseBody List<Route> viewAllRoutesInJSONFormat() {
        return routeDao.findAll();
    }

    @GetMapping("/searchRoutes")
    public String search(@RequestParam(name = "term") String term, Model model){
        term = "%"+term+"%";
        List searchRoutes = routeDao.findAllByTitleIsLike(term);
        model.addAttribute("routes", searchRoutes);
        return "routes/index";
    }

    @GetMapping("/route/{id}")
    public String viewPost(@PathVariable(name= "id") long id, Model model ) {
        model.addAttribute("route", routeDao.getOne(id));

        return "routes/showRoute";
    }
    @PostMapping("/route/{id}/delete")
    public String deleteRoute (@PathVariable(value = "id") long id) {
        Route routeToDelete = routeDao.getRouteById(id);
        routeDao.delete(routeToDelete);
        return "redirect:/profile";
    }
}
