package com.example.demo.controller;
import com.example.demo.model.Comment;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.h2.engine.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



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
    private UserService userService;
    @Autowired
    private User user;
    @Autowired
    private Event_dinService eventDinService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping("/events")
    public String listAllEvents(Model model, HttpServletRequest request){
        model.addAttribute("events",eventService.findAll());
        model.addAttribute("isAdmin", request.isUserInRole("ADMIN"));
        return "events";
    }
    @PostMapping("/events")
    public String listAllEventsDin(Model model, String artist,Double price){
        model.addAttribute("events",eventDinService.findAll(artist,price));
        return "events";
    }
    /*
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
        }



    }*/
    @PostConstruct
    public void init() throws IOException {
        eventService.cleandirectory();
    }

    @GetMapping("/events/{id}")
    public String showEvent(Model model,@PathVariable long id,HttpServletRequest request){
        Event event = eventService.findById(id);
        model.addAttribute("event", event);
        boolean isAdmin = request.isUserInRole("ADMIN");
        boolean find = false;
        if (request.getUserPrincipal() != null){
            String currentUser = request.getUserPrincipal().getName();
            User user = userService.findbyusername(currentUser);
            //model.addAttribute("commentUser",request.getUserPrincipal().getName());
            for (Comment comment : event.getComments()) {
                User commentUser = comment.getUser();
                comment.setBelongsToCurrentUser(commentUser.getId().equals(user.getId()) || isAdmin);
            }
            find = userService.haveEvent(user, event);
        }
        model.addAttribute("filename",imageService.getHashMap(id));
        model.addAttribute("isAdmin", isAdmin);
        boolean isUser = request.isUserInRole("USER");
        model.addAttribute("isUser", isUser);
        boolean ticketsLeft = event.getTicketsAvailable() > 0;
        model.addAttribute("ticketsLeft", ticketsLeft);
        model.addAttribute("haveEvent", find);
        return "eventTemplate";
    }

    @GetMapping("/events/new")
    public String createEvent(Model model){

        model.addAttribute("user",user);

        return "createEvent";
    }
    @PostMapping("/events/new")
    public String newEvent(Model model,Event newevent,MultipartFile imageField,MultipartFile pdffile)throws IOException{
        Event event=eventService.save(newevent,imageField);
        Long eid= newevent.getId();
        String pdfname = pdffile.getOriginalFilename();
        if (!pdffile.isEmpty()){
            if (pdfname.toLowerCase().endsWith(".pdf")){
                if (!(pdfname.contains("/") || pdfname.contains("..") || pdfname.contains("%") || pdfname.contains("\\"))){
                    imageService.savePdf(pdffile, eid);
                } else {
                    return "/error/pdferror2";
                }
            } else {
                return "/error/pdferror";
            }
        }
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
        Event post = eventService.findById(id);
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


    @PostMapping("/events/{id}/delete")
    public String deleteEvent(Model model, @PathVariable long id) throws IOException{
        Event event = eventService.findById(id);
        if (event != null) {
            eventService.delete(event);
            return "eventDeleted";
        } else {
            return "home";
        }
    }
    @PostMapping("/events/{id}/comments")
    public String submitComment(Model model, String content, int valoration, @PathVariable long id, HttpServletRequest request) {
        String sanitizeContent=eventService.sanitizexss(content);
        String username = request.getUserPrincipal().getName();
        User myUser=userService.findbyusername(username);
        Comment comment = new Comment(myUser, sanitizeContent, valoration);
        Event event1=eventService.findById(id);
        comment.setEventId(event1);
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
    public String changeEvent(Model model,Event updateEvent,MultipartFile imageField,@PathVariable long id,MultipartFile pdffile) throws IOException {
        eventService.edit(updateEvent,imageField);
        Long idd=updateEvent.getId();
        if (imageService.getHashMap(idd)!=null && !pdffile.isEmpty()){
            eventService.cleanMyDirectory(idd);
        }
        String pdfname = pdffile.getOriginalFilename();
        if (!pdffile.isEmpty()){
            if (pdfname.toLowerCase().endsWith(".pdf")){
                if (!(pdfname.contains("/")|| pdfname.contains("..")|| pdfname.contains("%")|| pdfname.contains("\\") )){
                    imageService.savePdf(pdffile, updateEvent.getId());}}}
        return "home";
    }


    @PostMapping("/buy/{id}")
    public String buyEvent(Model model,@PathVariable long id, HttpServletRequest request){
        model.addAttribute("user",user);
        eventService.buy(id,request);
        return "redirect:/events/{id}";
    }
    @GetMapping("/tickets")
    public String seeMyevents(Model model,HttpServletRequest request){

        String name = request.getUserPrincipal().getName();
        User user=userService.findbyusername(name);

        model.addAttribute("user",user);
        return "myEvents";
    }
    @PostMapping("/events/{id}/delete/{commentId}")
    public String deleteComment(Model model,@PathVariable long id,@PathVariable long commentId, HttpServletRequest request){
        Event event = eventService.findById(id);
        String username = request.getUserPrincipal().getName();
        boolean isAdmin = request.isUserInRole("ADMIN");
        User user = userService.findbyusername(username);
        if (event != null) {
            for (Comment comment : event.getComments()) {
                if (comment.getId() == commentId && (comment.getUser() == user || isAdmin)) {
                    event.getComments().remove(comment);
                    eventService.saveSimple(event);
                    break;
                }
            }
        }
        return "redirect:/events/{id}";
    }
    @GetMapping("/signup")
    public String signUp(Model model){
        return "signup";
    }
    @PostMapping("/signup")
    public String newuser(Model model,String username,String password){
        if (!userService.existname(username) && !username.isEmpty() && !password.isEmpty() && userService.checkPassword(password)){
            User newUser = new User(username, passwordEncoder.encode(password), "USER");
            userService.saveUserinDB(newUser);
            return "redirect:/login";
        } else {
            return "redirect:/signuperror";
        }
    }

    @PostMapping("/{id}/remove")
    public String removeEvent(Model model, HttpServletRequest request, @PathVariable Long id){
        if (request.getUserPrincipal() != null){
            String username = request.getUserPrincipal().getName();
            User user = userService.findbyusername(username);
            Event event = eventService.findById(id);
            eventService.removeEvent(event, user);
            eventService.oneMore(event);
        }
        return "redirect:/";
    }

}
