package com.example.demo;
import jakarta.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;
    private String content;
    private int valoration;
    private long eventId;

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

    public long getEventId() {
        return eventId;
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

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public void setId(long id) {
        this.id = id;
    }
}