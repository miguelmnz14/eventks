package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class DatabaseUsersLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EventService eventService;
/*
    @PostConstruct
    private void initDatabase() throws IOException {
        userRepository.save(new User("user", passwordEncoder.encode("pass"), "USER"));
        userRepository.save(new User("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));
        Event event= new Event("Gran evento", "La pareja valenciana ha logrado ser la nueva sensación del hip-hop sin sello, sin publicidad, sin mánager, sin contacto con la prensa y con una discretísima presencia en redes gracias a 'BBO', un disco mimado al detalle y repleto de talento, una obra generacional ", "Hoke", 20, 10);

        eventService.save(event,null);
        Event event1= new Event("Al Safir","Descripción: Diego Izquierdo, más conocido como “Al Safir”, es natural de la sierra de Madrid (1995). Su inquietud musical le ha llevado a experimentar un sonido atípico y configurarse como la promesa del underground nacional. Al Safir desvela ‘Black Ops’, el segundo adelanto de su próximo disco. El single comparte título con su nuevo álbum y supone un salto hacia sonidos más experimentales dentro del género urbano.","Diego Izquierdo",20,200);

        eventService.save(event1,null);
        Event event2=new Event("Kid Keo","Algunos de tus temas superan los diez millones de reproducciones en Spotify, como Kikiki o Ma Vie. Por no hablar de Dracukeo, tu single más célebre, cuyo vídeo supera las noventa millones de visitas en YouTube.","Padua Keoma",30,250);

        eventService.save(event2,null);

    }*/
}