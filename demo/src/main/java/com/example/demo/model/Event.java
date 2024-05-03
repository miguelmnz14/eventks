package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.sql.Blob;
import java.util.List;
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id = null;
    private String name;
    @Column(length = 1000)
    private String description;
    private String artists;
    private double price;
    private int ticketsAvailable;
    private String image;
    @OneToMany (mappedBy = "event",cascade = CascadeType.ALL ,orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<User> users;
    @Lob
    @JsonIgnore
    private Blob imageFile;
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
    public List<Comment> getComments(){
        return comments;
    }
    public Long getId(){return id;}
    public void setId(Long id){
        this.id=id;
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
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob image) {
        this.imageFile = image;
    }
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setEventId(this);
    }
    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setEventId(null);
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
