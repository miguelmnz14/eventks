package com.example.demo.controller;

import com.example.demo.service.CommentService;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BBDDController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepository;
    /*@PostConstruct
    public void init(){
        Event event = new Event("pet", "jajaj", "kka", 1, 3);
        event.setImage("null");

        // Guardar el evento en la base de datos
        eventRepository.save(event);

        // Crear un usuario y asignarle el evento
        List<Event> events = new ArrayList<>();
        events.add(event);
        User user = new User("peter", events);

        // Guardar el usuario en la base de datos
        userRepository.save(user);

    }*/

}
