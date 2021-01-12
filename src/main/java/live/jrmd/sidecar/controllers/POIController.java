package live.jrmd.sidecar.controllers;

import live.jrmd.sidecar.models.*;
import live.jrmd.sidecar.repositories.POICatRepository;
import live.jrmd.sidecar.repositories.POICommentRepository;
import live.jrmd.sidecar.repositories.POIRepository;
import live.jrmd.sidecar.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import javax.el.ELException;
import java.util.List;

@Controller
public class POIController {
    private final POIRepository poiDao;
    private final UserRepository userDao;
    private final POICatRepository pCatDao;
    private POICommentRepository poiCommentDao;

    public POIController(POIRepository poiDao, UserRepository userDao, POICatRepository pCatDao, POICommentRepository poiCommentDao) {
        this.poiDao = poiDao;
        this.userDao = userDao;
        this.pCatDao = pCatDao;
        this.poiCommentDao = poiCommentDao;
    }

    @GetMapping("/points")
    public String showAllPOIs(Model model){
        model.addAttribute("points", poiDao.findAll());
        try {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("location", userDb.getZipcode());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
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

        try {
            User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.addAttribute("location", userDb.getZipcode());
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        model.addAttribute("poi", new POI());
        return "points/create";
    }
    @PostMapping("/points/create")
    public String create(@ModelAttribute POI poiToBeSaved) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        poiToBeSaved.setUser(userDb);
        poiDao.save(poiToBeSaved);
        return "redirect:/points/";
    }
    @GetMapping("/points/{id}")
    public String singlePOI(@PathVariable long id, Model model) {
        model.addAttribute("point", poiDao.getPOIById(id));
        model.addAttribute("poiComments", poiCommentDao.findAllByPoiId(id));
        model.addAttribute("newComment", new POIComment());
        return "points/show";
    }
    @GetMapping("/points/{id}/edit")
    public String viewUpdatePOIForm(@PathVariable long id, Model model) {
        model.addAttribute("poi", poiDao.getPOIById(id));
        return "points/edit";
    }
    @PostMapping("/points/{id}/edit")
    public String updatePOI(@ModelAttribute POI poiToBeUpdated, @PathVariable long id) {
        User userDb = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        poiToBeUpdated.setUser(userDb);
        poiDao.save(poiToBeUpdated);
        return "redirect:/points/" + id;
    }
    @PostMapping("/points/{id}/delete")
    public String deletePoint (@PathVariable(value = "id") long id) {
        POI poiToDelete = poiDao.getPOIById(id);
        poiDao.delete(poiToDelete);
        return "redirect:/profile";
    }

    //Commenting
    @PostMapping("/points/{id}/comment")
    public String addComment(
            @PathVariable long id,
            @ModelAttribute POIComment newComment){
        POIComment comment = new POIComment();
        Date thisDate = new Date();
        POI poi = poiDao.getPOIById(id);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        comment.setComment(newComment.getComment());
        comment.setPoi(poi);
        comment.setUser(user);
        comment.setDate(thisDate);

        poiCommentDao.save(comment);

        return "redirect:/points/" + id;
    }

    @PostMapping("/points/{id}/delete-comment-{commentId}")
    public String deleteComment(
            @PathVariable(value = "id") long id,
            @PathVariable(value = "commentId") long commentId
    ){
        POIComment commentToDelete = poiCommentDao.getOne(commentId);
        poiCommentDao.delete(commentToDelete);
        return "redirect:/points/" + id;
    }
}
