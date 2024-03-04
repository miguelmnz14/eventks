package com.example.demo;

public class Event {
    private String name;
    private String description;
    private String artists;
    private double price;
    private int ticketsAvailable;
    public Event(){

    }
    public Event(String name,String description,String artists,double price,int ticketsAvailable){
        super();
        this.name=name;
        this.description=description;
        this.artists=artists;
        this.price=price;
        this.ticketsAvailable=ticketsAvailable;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getArtists(){
        return artists;
    }
    public void setArtists(String artists){
        this.artists=artists;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price=price;
    }
    public int getTicketsAvailable(){
        return ticketsAvailable;
    }
    public void setTicketsAvailable(int ticketsAvailable){
        this.ticketsAvailable=ticketsAvailable;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", artists='" + artists + '\'' +
                ", price=" + price +
                ", ticketsAvailable=" + ticketsAvailable +
                '}';
    }
}
