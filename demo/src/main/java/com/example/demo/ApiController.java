package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@RestController
@RequestMapping("/api/events")
public class ApiController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private User user;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId){
        Event event = eventService.findById(eventId);
        return ResponseEntity.ok(event);
    }
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        Event createdEvent = eventService.save(event, null);
        return ResponseEntity.ok(createdEvent);
    }

    @DeleteMapping(("/{eventId}"))
    public ResponseEntity<Event> deleteEvent(@PathVariable Long eventId){

        Event event = eventService.findById(eventId);

        if (event != null) {
            eventService.delete(eventId);
            return ResponseEntity.ok(event);
        } else {

            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> editEvent(@PathVariable Long eventId, @RequestBody Event event){
        Event aux = eventService.findById(eventId);
        Event eventEdited = eventService.edit1(event, eventId, aux);
        return ResponseEntity.ok(eventEdited);
    }
    @GetMapping("/{eventId}/comments")
    public ResponseEntity<List<Comment>> seeComments(@PathVariable Long eventId){
        Event event = eventService.findById(eventId);

        if (event != null) {
            List<Comment> comments = event.getComments();
            return ResponseEntity.ok(comments);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/{eventId}/comments")
    public ResponseEntity<Comment> createcomment(@PathVariable Long eventId,@RequestBody Comment comment){
        Event event=eventService.findById(eventId);
        eventService.addComment(event,comment);
        URI location = fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/{eventId}/comments/{id}")
    public ResponseEntity<Comment> deletecomment(@PathVariable Long eventId,@PathVariable Long id){
        Event event=eventService.findById(eventId);
        Comment comment=eventService.findCommentById(event,id);
        if (event != null && comment != null){
            eventService.deleteCommentById(event,id);
            return ResponseEntity.ok(comment);
        }else {
            return ResponseEntity.notFound().build();
        }


    }
    @PostMapping("/{eventID}/image")
    public ResponseEntity<Object> uploadImage(@PathVariable Long eventID, @RequestParam MultipartFile imageFile) throws IOException {
        Event event = eventService.findById(eventID);
        if (event != null){
            URI location = fromCurrentRequest().build().toUri();
            event.setImage(location.toString());
            eventService.edit(event,imageFile);
            Path FILES_FOLDER= Paths.get(System.getProperty("user.dir"), "images");
            imageService.saveImage(FILES_FOLDER.toString(),event.getId(),imageFile);
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{eventID}/image")
    public ResponseEntity<Object> editImage(@PathVariable Long eventID, @RequestParam MultipartFile imageFile) throws IOException {
        Event event = eventService.findById(eventID);
        if (event != null){
            URI location = fromCurrentRequest().build().toUri();

            event.setImage(location.toString());
            eventService.edit(event,imageFile);
            Path FILES_FOLDER= Paths.get(System.getProperty("user.dir"), "images");
            imageService.saveImage(FILES_FOLDER.toString(),event.getId(),imageFile);
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{eventID}/image")
    public ResponseEntity<Object> deleteImage(@PathVariable long eventID)
        throws IOException {
        Event event = eventService.findById(eventID);
        if(event != null) {
            event.setImage(null);
            Path FILES_FOLDER= Paths.get(System.getProperty("user.dir"), "images");
            imageService.deleteImage1(FILES_FOLDER.toString(),event.getId());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
