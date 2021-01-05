package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class UserController {

    private final UserRepository userDao;
    private final RouteRepository routeDao;
    private final POIRepository poiDao;
    private final EventRepository eventDao;
    private final UsersRepository users;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao,
                          RouteRepository routeDao,
                          POIRepository poiDao,
                          EventRepository eventDao,
                          UsersRepository users,
                          PasswordEncoder passwordEncoder
    ) {
        this.userDao = userDao;
        this.routeDao = routeDao;
        this.poiDao = poiDao;
        this.eventDao = eventDao;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/logout")
    public String logout(){
        //remove session user
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new User());
        return "users/register";
    }

    @PostMapping("/register")
    public String saveUser(@Valid User user,
                           Errors validation,
                           Model model,
                           @ModelAttribute User newUser
    ){
        if(validation.hasErrors()){
            model.addAttribute("errors", validation);
            model.addAttribute("user", user);
            return "/register";
        } else {
            if (newUser.getPassword().equals(newUser.getPassword_confirm())) {
                String hash = passwordEncoder.encode(newUser.getPassword());
                newUser.setPassword(hash);
                newUser.setPassword_confirm(hash);
            }
            users.save(newUser);
            return "redirect:/login";
        }
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userDao.getUserById(user.getId()));
        model.addAttribute("routes", routeDao.findAllByUser(user));
        model.addAttribute("pois", poiDao.findAllByUser(user));
        model.addAttribute("events", eventDao.findAllByUser(user));
        return "users/profile";
    }
    @GetMapping("/user/{id}/edit")
    public String editUser(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("userToEdit", userDao.getUserById(id));
        return "users/edit";
    }

    @PostMapping("/user/{id}/edit")
    public String editUser (@ModelAttribute User user){
        if (user.getPassword() != null && user.getPassword().equals(user.getPassword_confirm())) {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
            user.setPassword_confirm(hash);
        } else {
            user.setPassword(userDao.getUserById(user.getId()).getPassword());
        }
            userDao.save(user);
            return "redirect:/profile";

    }
}
