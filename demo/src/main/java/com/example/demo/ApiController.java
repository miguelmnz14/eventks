package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

@RestController
@RequestMapping("/api/events")
public class ApiController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ImageService imageService;

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
    public String deleteEvent(@PathVariable Long eventId){
        eventService.delete(eventId);
        return "deleted";
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> editEvent(@PathVariable Long eventId, @RequestBody Event event){
        Event aux = eventService.findById(eventId);
        Event eventEdited = eventService.edit1(event, eventId, aux);
        return ResponseEntity.ok(eventEdited);
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
