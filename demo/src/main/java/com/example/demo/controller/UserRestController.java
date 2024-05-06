package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.service.EventService;
import com.example.demo.service.UserUpdateRequest;
import com.example.demo.model.User;
import com.example.demo.security.jwt.JwtCookieManager;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtCookieManager jwtCookieManager;
    @Autowired
    private EventService eventService;

    @GetMapping("/me")
    public ResponseEntity<User> me(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();

        if(principal != null) {
            return ResponseEntity.ok(userService.findbyusername(principal.getName()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyUser(HttpServletRequest request) throws ServletException {
        if (request.getUserPrincipal()!=null){
            String username = request.getUserPrincipal().getName();
            User user = userService.findbyusername(username);
            List <Event> events = user.getMyEvents();
            for (Event event : events){
                eventService.oneMore(event);
                eventService.saveSimple(event);
            }
            userService.deleteUser(request.getUserPrincipal().getName());
            jwtCookieManager.deleteAccessTokenCookie();
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/me")
    public ResponseEntity<Void> editMyUser(HttpServletRequest request, @RequestBody UserUpdateRequest newUser){

        User user = userService.findbyusername(request.getUserPrincipal().getName());
        userService.editUser(request.getUserPrincipal().getName(), newUser.getUsername(), newUser.getPassword());
        return ResponseEntity.ok().build();


    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> seeAllUsers(HttpServletRequest request){
        List<User> users= userService.findAllUser();
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/all/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username){
        User user =userService.findbyusername(username);
        if(user!=null){
            List <Event> events = user.getMyEvents();
            for (Event event : events){
                eventService.oneMore(event);
                eventService.saveSimple(event);
            }
            userService.deleteUser(username);
            return ResponseEntity.ok().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/mytickets")
    public ResponseEntity<List<Event>> seeAllmyEvents(HttpServletRequest request){
        String name =request.getUserPrincipal().getName();
        User myUser=userService.findbyusername(name);
        List<Event> myevents= myUser.getMyEvents();
        if (!request.isUserInRole("ADMIN")){
            for (Event event: myevents){
                event.setUsers(null);
            }
        }
        return ResponseEntity.ok(myevents);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity <String> removeEvent (@PathVariable Long id, HttpServletRequest request){
        String username = request.getUserPrincipal().getName();
        User user = userService.findbyusername(username);
        Event event = eventService.findById(id);
        if (user.getMyEvents().contains(event)){
            eventService.removeEvent(event, user);
            eventService.oneMore(event);
        }

        return ResponseEntity.ok().build();
    }
}
