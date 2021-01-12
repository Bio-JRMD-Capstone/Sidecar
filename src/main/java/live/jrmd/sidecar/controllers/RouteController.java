package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.*;
import live.jrmd.sidecar.repositories.RouteCommentRepository;
import live.jrmd.sidecar.repositories.RouteRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class RouteController {
    private final RouteRepository routeDao;
    private RouteCommentRepository routeCommentDao;
    private final UserRepository userDao;

    public RouteController(RouteRepository routeDao,RouteCommentRepository routeCommentDao, UserRepository userDao){
        this.routeDao = routeDao;
        this.routeCommentDao = routeCommentDao;
        this.userDao = userDao;
    }

    @GetMapping("/routes/create")
    public String buildARoute(Model model){
        try {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("location", userDb.getZipcode());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        model.addAttribute("route", new Route());
        return "routes/create";
    }

    @PostMapping("/routes/create")
    public String saveRoute(@ModelAttribute Route route, @Valid Route routeVal, Errors validation, Model model){
        if (validation.hasErrors()) {
            model.addAttribute("errors", validation);
            model.addAttribute("route", routeVal);
            return "routes/create";
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        route.setUser(user);
        routeDao.save(route);
        return "redirect:/routes";
    }


    @GetMapping("/routes")
    public String showAllRoutes(Model model){
        try {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("location", userDb.getZipcode());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
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
//        Route route = routeDao.getRouteById(id);
//        List routeComments = routeCommentDao.findAllByRouteId(id);
        model.addAttribute("route", routeDao.getRouteById(id));
        model.addAttribute("routeComments", routeCommentDao.findAllByRouteId(id));
        model.addAttribute("newComment", new RouteComment());
        return "routes/showRoute";
    }

    @GetMapping("/route/{id}/edit")
    public String editRoute(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("routeToEdit", routeDao.getRouteById(id));
        return "routes/edit";
    }

    @PostMapping("/route/{id}/edit")
    public String editRoute(
            @Valid Route route,
            Errors validation,
            Model model,
            @ModelAttribute Route amendedRoute) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(validation.hasErrors()){
            model.addAttribute("errors", validation);
            model.addAttribute("route", route);
            return "route/{id}/edit";
        } else {
            amendedRoute.setUser(user);
            routeDao.save(amendedRoute);
            return "redirect:/route/" + route.getId();
        }
    }

    @PostMapping("/route/{id}/delete")
    public String deleteRoute (@PathVariable(value = "id") long id) {
        Route routeToDelete = routeDao.getRouteById(id);
        routeDao.delete(routeToDelete);
        return "redirect:/profile";
    }

    //Commenting
    @PostMapping("/route/{id}/comment")
    public String addComment(
            @PathVariable long id,
            @ModelAttribute RouteComment newComment){
        RouteComment comment = new RouteComment();
        Date thisDate = new Date();
        Route route = routeDao.getRouteById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        comment.setComment(newComment.getComment());
        comment.setRoute(route);
        comment.setUser(user);
        comment.setDate(thisDate);

        routeCommentDao.save(comment);

        return "redirect:/route/" + id;
    }

    @PostMapping("/route/{id}/delete-comment-{commentId}")
    public String deleteComment(
            @PathVariable(value = "id") long id,
            @PathVariable(value = "commentId") long commentId
    ){
        RouteComment commentToDelete = routeCommentDao.getOne(commentId);
        routeCommentDao.delete(commentToDelete);
        return "redirect:/route/" + id;
    }

}
