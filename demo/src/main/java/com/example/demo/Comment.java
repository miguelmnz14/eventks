package com.example.demo;

public class Comment {
    private String name;
    private String content;
    private int valoration;
    private int eventId;

    public Comment(){

    }
    public Comment(String name, String content, int valoration){
        super();
        this.name=name;
        this.content=content;
        this.valoration=valoration;
    }

    public int getValoration() {
        return valoration;
    }

    public String getContent() {
        return content;
    }
    public String getName(){
        return name;
    }

    public int getEventId() {
        return eventId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValoration(int valoration) {
        this.valoration = valoration;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
