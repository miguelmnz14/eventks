package com.example.demo;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public String procesarFormulario(@RequestParam String username, Model model) {
        this.user.setUsername(username);
        model.addAttribute("username", username);
        return "userRegistered";
    }

}

