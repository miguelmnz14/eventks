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
@Entity
@Scope(proxyMode = ScopedProxyMode.NO)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;
    private String username;

    @ManyToMany(mappedBy="users")
    @JsonIgnore
    private List<Event> myEvents;
    public User(){

    }

    public User(String username,List<Event> myEvents){
        this.username=username;
        this.myEvents=myEvents;
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
}
