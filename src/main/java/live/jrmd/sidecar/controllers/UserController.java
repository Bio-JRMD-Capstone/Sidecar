package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {

    @Value("${fileStackAPI.key}")
    private String fileStackApiKey;

    private final UserRepository userDao;
    private final RouteRepository routeDao;
    private final POIRepository poiDao;
    private final EventRepository eventDao;
    private final UsersRepository users;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository userDao, RouteRepository routeDao, POIRepository poiDao, EventRepository eventDao, UsersRepository users, PasswordEncoder passwordEncoder) {
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
    public String saveUser(@ModelAttribute User user){
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        users.save(user);
        return "redirect:/login";
    }

    @GetMapping("/keys.js")
    @ResponseBody
    public String apikey(){
        System.out.println(fileStackApiKey);
        return "const FileStackApiKey = \"" + fileStackApiKey + "\"";
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
}
