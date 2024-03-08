package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class ApiController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event){
        Event createdEvent = eventService.save(event, null);
        return ResponseEntity.ok(createdEvent);
    }

    @DeleteMapping(("/{eventId}"))
    public String deleteEvent(@PathVariable Long eventId){
        eventService.delete(eventId);
        return "lo borraste";
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<Event> editEvent(@PathVariable Long eventId, @RequestBody Event event){
        Event eventEdited = eventService.edit1(event, eventId);
        return ResponseEntity.ok(eventEdited);
    }
}
