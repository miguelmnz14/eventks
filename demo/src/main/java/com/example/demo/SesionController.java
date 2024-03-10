package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;



@Controller
public class SesionController {
    private User user;
    private String sharedInfo;
    @PostMapping("/processForm")
    public String processForm(@RequestParam String info) {

        this.user.setInfo(info);
        this.sharedInfo = info;

        return "result_form";
    }
    @GetMapping("/showData")
    public String showData(Model model) {

        String infoUser = this.user.getInfo();

        model.addAttribute("infoUser", infoUser);
        model.addAttribute("sharedInfo", this.sharedInfo);

        return "data";
    }
}

