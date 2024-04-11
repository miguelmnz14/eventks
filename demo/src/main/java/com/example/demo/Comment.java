package com.example.demo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String content;
    private int valoration;
    @JsonIgnore
    @ManyToOne
    private Event event;

    public Comment() {

    }

    public Comment(String username, String content, int valoration) {
        super();
        this.username = username;
        this.content = content;
        this.valoration = valoration;
    }

    public int getValoration() {
        return valoration;
    }

    public String getContent() {
        return content;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
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
}