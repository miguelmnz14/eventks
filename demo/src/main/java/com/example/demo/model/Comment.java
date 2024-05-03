package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String content;
    private int valoration;
    @JsonIgnore
    @ManyToOne
    private Event event;
    @ManyToOne
    private User user;

    public Comment() {

    }

    public Comment(User user, String content, int valoration) {
        super();
        this.user=user;
        this.content = content;
        this.valoration = valoration;
    }

    public int getValoration() {
        return valoration;
    }

    public String getContent() {
        return content;
    }



    public Event getEventId() {
        return event;
    }

    public long getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public void setValoration(int valoration) {
        this.valoration = valoration;
    }

    public void setEventId(Event event) {
        this.event = event;
    }

    public void setId(long id) {
        this.id = id;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}