package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class EntradaLog {
    @GetMapping("/")
    public String entrada() {
        return "entrada";
    }

}
