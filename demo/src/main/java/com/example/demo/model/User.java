package com.example.demo.model;
import com.example.demo.model.Event;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import jakarta.persistence.*;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.List;

@Component
@SessionScope
@Entity(name = "USERS")
@Scope(proxyMode = ScopedProxyMode.NO)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String username;

    @ManyToMany(mappedBy="users")
    @JsonIgnore
    private List<Event> myEvents;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> comments;
    @JsonIgnore
    private String encodedPassword;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;
    public User(){

    }
    public User(String username,String encodedPassword,String... roles){
        this.username=username;
        this.encodedPassword=encodedPassword;
        this.roles=List.of(roles);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }
}

