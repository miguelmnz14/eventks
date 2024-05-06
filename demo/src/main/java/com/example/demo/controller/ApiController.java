package com.example.demo.controller;

import com.example.demo.model.Comment;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.EventService;
import com.example.demo.service.Event_dinService;
import com.example.demo.service.ImageService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
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
    @Autowired
    private User user;
    @Autowired
    Event_dinService eventDinService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(HttpServletRequest request){

        List<Event> events = eventService.findAll();
        if (!request.isUserInRole("ADMIN")){
            for (Event event:events){
                event.setUsers(null);
            }
        }
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable Long eventId,HttpServletRequest request){
        Event event = eventService.findById(eventId);
        if (!request.isUserInRole("ADMIN")){
            event.setUsers(null);
        }
        return ResponseEntity.ok(event);
    }
    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws IOException {
        Event createdEvent = eventService.save(event, null);
        return ResponseEntity.ok(createdEvent);
    }

    @DeleteMapping(("/{eventId}"))
    public ResponseEntity deleteEvent(@PathVariable Long eventId) throws IOException {
        Event event = eventService.findById(eventId);
        Event aux = event;
        if (event != null) {
            eventService.delete(event);
            return ResponseEntity.ok("Event " + eventId + " successfully removed.");
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
    public ResponseEntity<Comment> createcomment(@PathVariable Long eventId,@RequestBody Comment comment,HttpServletRequest request){
        String cont= comment.getContent();
        String newcont=eventService.sanitizexss(cont);
        comment.setContent(newcont);
        User user =userService.findbyusername(request.getUserPrincipal().getName());
        comment.setUser(user);
        Event event=eventService.findById(eventId);
        comment.setEventId(event);
        eventService.addComment(event,comment);
        URI location = fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/{eventId}/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long eventId, @PathVariable Long id, HttpServletRequest request) {
        Event event = eventService.findById(eventId);
        Principal principal = request.getUserPrincipal();
        User user;
        if(principal != null){
            user = userService.findbyusername(principal.getName());
        } else {
            return ResponseEntity.notFound().build();
        }
        int i = 0;
        if (event != null) {
            List<Comment> comments = event.getComments();
            for (Comment comment : comments) {
                if (comment.getId()==id && (comment.getUser().equals(user) || request.isUserInRole("ADMIN"))) {
                    comments.remove(comment);
                    event.setComments(comments);
                    eventService.saveSimple(event);
                    i = 1;
                    break;
                }
            }
            if (i == 0) {
                return ResponseEntity.notFound().build(); // El comentario no existe
            } else {
                return ResponseEntity.ok().build();
            }
        } else {
            return ResponseEntity.notFound().build(); // El evento no existe
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
            event.setImageFile(null);
            eventService.save(event,null);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/din")
    public ResponseEntity<List<Event>> findDin (String artist, Double price){
        return ResponseEntity.ok(eventDinService.findAll(artist, price));
    }
    @PostMapping("/buy/{id}")
    public ResponseEntity<Void> buyEvent(HttpServletRequest request,@PathVariable Long id){
        eventService.buy(id,request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pdf/{id}")
    public ResponseEntity<String> editPdf (@PathVariable Long id, @RequestBody(required = false) MultipartFile pdfFile) throws IOException {
        imageService.deleteImage2(id);
        if (pdfFile != null) {
            String pdfname = pdfFile.getOriginalFilename();
            if (!pdfFile.isEmpty()) {
                if (pdfname.toLowerCase().endsWith(".pdf")) {
                    if (!(pdfname.contains("/") || pdfname.contains("..") || pdfname.contains("%") || pdfname.contains("\\"))) {
                        imageService.savePdf(pdfFile, id);
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El pdf contiene patrones no permitidos.");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo parece no ser un pdf.");
                }

            }
        }
        return ResponseEntity.ok().build();
    }
    @PostMapping("/pdf/{id}")
    public ResponseEntity<String> postPdf (@PathVariable Long id, @RequestBody(required = false) MultipartFile pdfFile) throws IOException {
        if (pdfFile != null){
            if(!imageService.havePdf(id)){
                String pdfname = pdfFile.getOriginalFilename();
                if (!pdfFile.isEmpty()){
                    if (pdfname.toLowerCase().endsWith(".pdf")){
                        if (!(pdfname.contains("/") || pdfname.contains("..") || pdfname.contains("%")|| pdfname.contains("\\"))){
                            imageService.savePdf(pdfFile, id);
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El pdf contiene patrones no permitidos.");
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El archivo parece no ser un pdf.");
                    }
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El evento ya dispone de pdf.");
            }
        }
        return ResponseEntity.ok().build();
    }
}
