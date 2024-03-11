package com.example.demo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EventController {
    private static final String POSTS_FOLDER = "events";
    @Autowired
    private EventService eventService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private User user;




    @GetMapping("/events")
    public String listAllEvents(Model model){
        model.addAttribute("events",eventService.findAll());
        return "events";
    }
    @PostConstruct
    public void init(){
        Event event= new Event("Gran evento", "un gran evento", "Hoke", 20, 10);
        event.setImage("image_imagenDWS.jpeg");
        eventService.save(event,null);
        if (user==null){
            user=new User("peterparker",null);
            user.setMyEvents(new ArrayList<>());
        }



    }

    @GetMapping("/events/{id}")
    public String showEvent(Model model,@PathVariable long id){
        Event event = eventService.findById(id);
            model.addAttribute("event", event);
            model.addAttribute("user",user);
            return "eventTemplate";
        }

    @GetMapping("/events/new")
    public String createEvent(Model model){

        model.addAttribute("user",user);

        return "createEvent";
    }
    @PostMapping("/events/new")
    public String newEvent(Model model,Event newevent,MultipartFile imageField)throws IOException{
        Event event=eventService.save(newevent,imageField);
        return "eventSubmitted";
    }
    @GetMapping("/events/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {
        Event event = eventService.findById(id);
        if (event != null) {
            Resource poster = imageService.getImage(event.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
        } else {

            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/events/{id}/delete")
    public String deleteEvent(Model model, @PathVariable long id) {
        Event event = eventService.findById(id);
        if (event != null) {
            eventService.delete(id);
            return "eventDeleted";
        } else {
            return "home";
        }
    }
    @PostMapping("/events/{id}/comments")
    public String submitComment(Model model, String username, String content, int valoration, @PathVariable long id) {
        Comment comment = new Comment(this.user.getUsername(), content, valoration);
        comment.setEventId(id);
        Event event =eventService.findById(id);
        eventService.addComment(event, comment);
        return "redirect:/events/"+event.getId();
    }




    @GetMapping("/events/{id}/edit")
    public String modifyEvent(Model model, @PathVariable long id) {
        Event event = eventService.findById(id);
        if (event != null) {
            model.addAttribute("event", event);
            return "modifyEvent";
        } else {
            return "home";
        }
    }

    @PostMapping("/events/{id}/edit")
    public String changeEvent(Model model,Event updateEvent,MultipartFile imageField,@PathVariable long id){
        eventService.edit(updateEvent,imageField);
        return "eventTemplate";
    }

    @GetMapping("/myuser")
    public String myUser(Model model){
        model.addAttribute("user",user);
        return "myUser";}
    @GetMapping("/buy/{id}")
    public String buyEvent(Model model,@PathVariable long id){
        model.addAttribute("user",user);
        eventService.buy(id);
        return "redirect:/events/{id}";
    }
    @GetMapping("/tickets")
    public String seeMyevents(Model model){
        model.addAttribute("user",user);
        return "myEvents";
    }
    @GetMapping("/events/{id}/delete/{commentId}")
    public String deleteComment(Model model,@PathVariable long id,@PathVariable long commentId){
        Event event=eventService.findById(id);
        eventService.deleteCommentById(event,commentId);
        return "redirect:/events/{id}";

    }

}
