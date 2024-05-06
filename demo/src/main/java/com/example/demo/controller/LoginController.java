package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.security.jwt.AuthResponse;
import com.example.demo.security.jwt.LoginRequest;
import com.example.demo.security.jwt.UserLoginService;
import com.example.demo.service.UserService;
import com.example.demo.service.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserLoginService userService;
    @Autowired
    private UserService userrrSerivec;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginRequest loginRequest) {

        return userService.login(loginRequest, accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {

        return userService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

        return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, userService.logout(request, response)));
    }
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserUpdateRequest myUSer){
        if (userrrSerivec.checkPassword(myUSer.getPassword())){
            if (!userrrSerivec.existname(myUSer.getUsername()) && !myUSer.getUsername().isEmpty() && !myUSer.getPassword().isEmpty()) {
                User newUser = new User(myUSer.getUsername(), passwordEncoder.encode(myUSer.getPassword()), "USER");
                userrrSerivec.saveUserinDB(newUser);
                return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.SUCCESS, "Creacion Correcta", null));

            } else {
                return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.FAILURE, "El nombre de usuario no esta disponible", null));
            }
        } else {
            return ResponseEntity.ok(new AuthResponse(AuthResponse.Status.FAILURE, "La contraseña no es suficientemente segura.", null));
        }
    }

}
