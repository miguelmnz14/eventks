package com.example.demo;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
    @PostConstruct
    public void init(){
        Event event=new Event("pet","jajaj","kka",1,3);
        event.setImage("null");
        eventRepository.save(event);
        List <Event> eventi=new ArrayList<>();
        User user=new User("peter",eventi);
        userRepository.save(user);
    }


}
