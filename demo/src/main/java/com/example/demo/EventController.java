package com.example.demo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/events")
    public String listAllEvents(Model model){
        model.addAttribute("events",eventService.findAll());
        return "events";
    }
    @PostConstruct
    public void init(){
        Event event= new Event("eajaj", "estoesunamierda", "peterparker", 20, 10);
        event.setImage("image_ce49fb04-2fb4-4f4c-a4ee-c7377a32cd12_WhatsApp Image 2024-02-14 at 09.45.43.jpeg");
        eventService.save(event,null);



    }

    @GetMapping("/events/{id}")
    public String showEvent(Model model,@PathVariable long id){
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            return "eventTemplate";
        } else {
            return "events";
        }
    }
    @GetMapping("/events/new")
    public String createEvent(Model model){
        return "createEvent";
    }
    @PostMapping("/events/new")
    public String newEvent(Model model,Event newevent,MultipartFile imageField)throws IOException{

        Event event=eventService.save(newevent,imageField);



        return "eventSubmitted";
    }
    @GetMapping("/events/{id}/image")
    public ResponseEntity<Object> downloadImage(@PathVariable long id) throws SQLException {

        Optional<Event> op = eventService.findById(id);

        if(op.isPresent()) {
            Event event = op.get();
            Resource poster = imageService.getImage(event.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(poster);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Film not found");
        }
    }

    @GetMapping("/events/{id}/delete")
    public String deleteEvent(Model model,@PathVariable long id) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            eventService.delete(id);
            return "eventDeleted";
        } else {
            return "home";
        }
    }


    @GetMapping("/events/{id}/edit")
    public String modifyEvent(Model model,@PathVariable long id){
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            model.addAttribute("id", id);
            return "modifyEvent";
        }else {
            return "home";
        }
    }
    @PostMapping("/events/{id}/edit")
    public String changeEvent(Model model,Event updateEvent,MultipartFile imageField,@PathVariable long id){
        eventService.edit(updateEvent,imageField);
        return "eventTemplate";
    }


}
