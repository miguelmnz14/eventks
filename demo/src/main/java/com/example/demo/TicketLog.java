package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class TicketLog {
    @GetMapping("/")
    public String entrada() {
        return "home";
    }
    @GetMapping("/artist")
    public String artist(){ return "artists";}

    /* @GetMapping("/events")
    public String events(){return "events";}*/
}

