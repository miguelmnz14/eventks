package com.example.demo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.core.io.InputStreamResource;
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
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Event_dinService eventDinService;




    @GetMapping("/events")
    public String listAllEvents(Model model){
        model.addAttribute("events",eventRepository.findAll());
        return "events";
    }
    @PostMapping("/events")
    public String listAllEventsDin(Model model, String artist,double price){
        model.addAttribute("events",eventDinService.findAll(artist,price));
        return "events";
    }
    @PostConstruct
    public void init() throws IOException {
        Event event= new Event("Gran evento", "La pareja valenciana ha logrado ser la nueva sensación del hip-hop sin sello, sin publicidad, sin mánager, sin contacto con la prensa y con una discretísima presencia en redes gracias a 'BBO', un disco mimado al detalle y repleto de talento, una obra generacional ", "Hoke", 20, 10);
        event.setImage("image_imagenDWS.jpeg");
        eventService.save(event,null);
        Event event1= new Event("Al Safir","Descripción: Diego Izquierdo, más conocido como “Al Safir”, es natural de la sierra de Madrid (1995). Su inquietud musical le ha llevado a experimentar un sonido atípico y configurarse como la promesa del underground nacional. Al Safir desvela ‘Black Ops’, el segundo adelanto de su próximo disco. El single comparte título con su nuevo álbum y supone un salto hacia sonidos más experimentales dentro del género urbano.","Diego Izquierdo",20,200);
        event1.setImage("image_WhatsApp Image 2024-03-12 at 09.10.52.jpeg");
        eventService.save(event1,null);
        Event event2=new Event("Kid Keo","Algunos de tus temas superan los diez millones de reproducciones en Spotify, como Kikiki o Ma Vie. Por no hablar de Dracukeo, tu single más célebre, cuyo vídeo supera las noventa millones de visitas en YouTube.","Padua Keoma",30,250);
        event2.setImage("image_WhatsApp Image 2024-03-12 at 09.19.15.jpeg");
        eventService.save(event2,null);
        /*if (user==null){
            user=new User("peterparker",null);
            user.setMyEvents(new ArrayList<>());
        }*/



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
    @GetMapping("/{id}/image")
    public ResponseEntity<Object> downloadImagess(@PathVariable long id)
            throws SQLException {
        Event post = eventRepository.findById(id).orElseThrow();
        if (post.getImageFile() != null) {
            Resource file = new InputStreamResource(
                    post.getImageFile().getBinaryStream());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                    .contentLength(post.getImageFile().length())
                    .body(file);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/events/{id}/delete")
    public String deleteEvent(Model model, @PathVariable long id) {
        Event event = eventService.findById(id);
        if (event != null) {
            eventService.delete(event);
            return "eventDeleted";
        } else {
            return "home";
        }
    }
    @PostMapping("/events/{id}/comments")
    public String submitComment(Model model, String username, String content, int valoration, @PathVariable long id) {
        Comment comment = new Comment(username, content, valoration);
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
        return "home";
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
        long newid=1;
        Optional<User> optionalUser = userRepository.findById(newid);
        User usuario =optionalUser.get();
        model.addAttribute("user",usuario);
        return "myEvents";
    }
    @GetMapping("/events/{id}/delete/{commentId}")
    public String deleteComment(Model model,@PathVariable long id,@PathVariable long commentId){
        Event event=eventService.findById(id);
        eventService.deleteCommentById(event,commentId);
        return "redirect:/events/{id}";

    }

}
