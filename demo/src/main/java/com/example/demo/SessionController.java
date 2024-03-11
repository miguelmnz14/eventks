package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;



@Controller
public class SessionController {
    @Autowired
    private User user;
    private String sharedInfo;
    @PostMapping("/procesarFormulario")
    public String procesarFormulario(@RequestParam String info, Model model) {
        this.user.setInfo(info);
        model.addAttribute("info", info);
        return "userRegistered";
    }
    @GetMapping("/mostrarDatos")
    public String mostrarDatos(Model model, HttpSession session) {
        String userinfo = (String) session.getAttribute("info");
        model.addAttribute("info", userinfo);
        model.addAttribute("infoCompartida", sharedInfo);
        return "userRegistered";
    }
}

