package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class webController {
    @Autowired
    private UserService userService;
    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if(principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("username", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

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

        /*String name = request.getUserPrincipal().getName();


        User user = userService.findbyusername(name);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("admin", request.isUserInRole("ADMIN"));*/

        return "private";
    }

    @GetMapping("/admin")
    public String admin(Model model) {

        return "admin";
    }
    @GetMapping("/admin/users")
    public String viewUsers(Model model){
        model.addAttribute("allusers",userService.findAllUser());
        return "adminAllUsers";
    }
    @GetMapping("/admin/users/{username}")
    public String deleteUser(@PathVariable String username){
        userService.deleteUser(username);
        return "redirect:/admin/users";
    }
    @GetMapping("/private/myuser")
    public String myUser(Model model,HttpServletRequest request){
        String name=request.getUserPrincipal().getName();
        User user = userService.findbyusername(name);
        model.addAttribute("username", user.getUsername());
        return "myUser";
    }
    @GetMapping("/private/myuser/delete")
    public String deletemyUser(Model model,HttpServletRequest request){
        String username=request.getUserPrincipal().getName();

        userService.deleteUser(username);
        return "redirect:/logout";
    }
    @PostMapping("/private/myuser/edit")
    public String editMyUser(Model model,HttpServletRequest request,String newUser,String password){
        String username=request.getUserPrincipal().getName();
        userService.editUser(username,newUser,password);
        return "redirect:/logout";
    }

}
