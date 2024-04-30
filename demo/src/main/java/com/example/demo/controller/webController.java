package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
@Controller
public class webController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    @GetMapping("/private")
    public String privatePage(Model model, HttpServletRequest request) {

        String name = request.getUserPrincipal().getName();

        User user = (User) userRepository.findByUsername(name).orElseThrow();

        model.addAttribute("username", user.getUsername());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));

        return "private";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}
