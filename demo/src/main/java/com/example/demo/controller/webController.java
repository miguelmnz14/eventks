package com.example.demo.controller;

import com.example.demo.model.Event;
import com.example.demo.model.User;

import com.example.demo.service.EventService;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class webController {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
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
    public String login(Model model, HttpServletRequest request) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        model.addAttribute("token", token.getToken());

        return "login";
    }
    @GetMapping("/loginerror")
    public String loginerror() {
        return "/error/loginerror";
    }

    @GetMapping("/signuperror")
    public String signuperror() { return "/error/signuperror"; }

    @GetMapping("/private")
    public String privatePage(Model model, HttpServletRequest request) {
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
        User user = userService.findbyusername(username);
        List<Event> events = user.getMyEvents();
        for (Event event : events){
            eventService.oneMore(event);
            eventService.saveSimple(event);
        }
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
    public String deletemyUser(Model model,HttpServletRequest request) throws ServletException {
        String username=request.getUserPrincipal().getName();

        userService.deleteUser(username);
        request.logout();
        return "redirect:/";
    }
    @PostMapping("/private/myuser/edit")
    public String editMyUser(Model model,HttpServletRequest request,String newUser,String password) throws ServletException {
        if (!newUser.equals(request.getUserPrincipal().getName())){
            if(userService.existname(newUser) || !userService.checkPassword(password)){
                return "error/changeNameError";
            }
        }
        if (userService.checkPassword(password)){
            String username = request.getUserPrincipal().getName();
            userService.editUser(username, newUser, password);
            request.logout();
            return "redirect:/login";
        } else {
            return "error/changeNameError";
        }
    }
}