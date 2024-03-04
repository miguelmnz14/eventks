package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


import java.util.ArrayList;
import java.util.List;
@Controller
public class EventController {
    private List<Event> events = new ArrayList<>();
    public EventController() {
        events.add(new Event("Ejemplo", "Descripccion del ejemplo", "artista del ejemplo",20,30));
        events.add(new Event("Esto es otro ej", "Descripccion del otroejemplo", "peter parker",10.3,30));
    }
    @GetMapping("/events")
    public String listAllEvents(Model model){
        model.addAttribute("events",events);
        return "events";
    }
    @GetMapping("/events/{numEvent}")
    public String showEvent(Model model,@PathVariable int numEvent){
        Event event = events.get(numEvent-1);
        model.addAttribute("event",event);
        return "eventTemplate";

    }
    @GetMapping("/events/new")
    public String createEvent(Model model){
        return "createEvent";
    }
    @PostMapping("/events/new")
    public String newEvent(Model model,Event event){
        events.add(event);
        return "eventSubmitted";
    }
}
