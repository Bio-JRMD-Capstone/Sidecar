package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.Event;
import live.jrmd.sidecar.models.User;
import live.jrmd.sidecar.repositories.EventCatRepository;
import live.jrmd.sidecar.repositories.EventRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class EventController {
    private final EventRepository eventDao;
    private final UserRepository userDao;
    private final EventCatRepository eCatDao;

    public EventController(EventRepository eventDao, UserRepository userDao, EventCatRepository eCatDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.eCatDao = eCatDao;
    }

    @GetMapping("/events")
    public String showAllEvents(Model model){
        model.addAttribute("events", eventDao.findAll());
        return "events/index";
    }
    @GetMapping("/searchEvents")
    public String search(@RequestParam(name = "term") String term, Model model){
        term = "%"+term+"%";
        List searchEvents = eventDao.findAllByNameIsLike(term);
        model.addAttribute("routes", searchEvents);
        return "events/index";
    }

    @GetMapping("/events/create")
    public String showCreateEventForm(Model model){
        model.addAttribute("event", new Event());
        return "events/create";
    }
    @PostMapping("/events/create")
    public String submitEvent(
            @Valid Event event,
            Errors validation,
            Model model,
            @ModelAttribute Event newEvent
    ){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(validation.hasErrors()){
            model.addAttribute("errors", validation);
            model.addAttribute("event", newEvent);
            return "events/create";
        } else {
            newEvent.setUser(user);
            eventDao.save(newEvent);
            return "redirect:/events";
        }
    }
    @GetMapping("/event/{id}")
    public String showIndividualEvent (@PathVariable(value = "id") long id, Model model){
        model.addAttribute("event", eventDao.getEventById(id));
        return "/events/showEvent";
    }
    @GetMapping("/event/{id}/edit")
    public String editEvent(@PathVariable(value = "id") long id, Model model) {
        model.addAttribute("eventToEdit", eventDao.getEventById(id));
        return "/events/edit";
    }
    @PostMapping("/event/{id}/edit")
    public String editEvent(@ModelAttribute Event event) {
        eventDao.save(event);
        return "redirect:/event/" + event.getId();
    }
    @PostMapping("/event/{id}/delete")
    public String deleteEvent (@PathVariable(value = "id") long id) {
        Event event = eventDao.getEventById(id);
        eventDao.delete(event);
        return "redirect:/profile";
    }
}
