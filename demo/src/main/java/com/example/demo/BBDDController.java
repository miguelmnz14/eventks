package com.example.demo;

import ch.qos.logback.classic.spi.ConfiguratorRank;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class BBDDController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CommentService commentService;
    @PostConstruct
    public void init(){
        Event event=new Event("pet","jajaj","kka",1,3);
        eventRepository.save(event);
    }


}
