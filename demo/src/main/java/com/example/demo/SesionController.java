package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;



@Controller
public class SesionController {
    private User user;
    private String infoCompartida;
    @PostMapping("/procesarFormulario")
    public String procesarFormulario(@RequestParam String info) {

        this.user.setInfo(info);
        infoCompartida = info;

        return "resultado_formulario";
    }
    @GetMapping("/mostrarDatos")
    public String mostrarDatos(Model model) {

        String infoUsuario = user.getInfo();

        model.addAttribute("infoUsuario", infoUsuario);
        model.addAttribute("infoCompartida", infoCompartida);

        return "datos";
    }
}

