package com.example.demo;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EventService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private User user;
    @Autowired
    private EventRepository eventRepository;
    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Event> events = new ConcurrentHashMap<>();
    public Event findById(long id) {
        return this.events.get(id);
    }

    public List<Event> findAll() {
        return this.events.values().stream().toList();
    }
    public Event save(Event event, MultipartFile imageField) {
        if (imageField != null && !imageField.isEmpty()){
            String path = imageService.createImage(imageField);
            event.setImage(path);
        }
        double price = event.getPrice();
        if (price <= 0){
            throw new InvalidPriceException("El precio del evento debe ser mayor que cero.");
        }

        return eventRepository.save(event);
    }
    public void addComment(Event event, Comment comment){
        if (event.getComments() == null) {
            event.setComments(new ArrayList<>());
        }
        List<Comment> comments = event.getComments();
        int size=comments.size()-1;
        comment.setId(size+1);
        comments.add(comment);
        event.setComments(comments);

    }



    public Event edit(Event event,MultipartFile imageField) {
        if (imageField != null && !imageField.isEmpty()){
            String path = imageService.createImage(imageField);
            event.setImage(path);
        }else {

            Event existingEvent = events.get(event.getId());
            if (existingEvent != null && existingEvent.getImage() != null) {
                event.setImage(existingEvent.getImage());
            }
        }
        Event existingEvent = events.get(event.getId());
        if (existingEvent != null) {
            event.setComments(existingEvent.getComments());
        }
        long id = event.getId();
        events.put(id, event);
        return event;
    }
    public Event edit1(Event event, Long id, Event aux){
        String image = aux.getImage();
        event.setImage(image);
        event.setComments(aux.getComments());
        events.put(id, event);
        event.setId(id);
        return event;
    }
    public void delete(long id) {
        Event deletedEvent = this.events.get(id);
        if (deletedEvent.getImage() != null){
            imageService.deleteImage(deletedEvent.getImage());
        }
        this.events.remove(id);
    }
    public void buy(long id){
        Event event=findById(id);
        if (user.getMyEvents() == null) {
            user.setMyEvents(new ArrayList<>());
        }
        if (event.getTicketsAvailable()>0 && !user.getMyEvents().contains(event)){
            if (event.getUsers() == null) {
                event.setUsers(new ArrayList<>());
            }


            List<User> users = event.getUsers();
            users.add(user);
            event.setUsers(users);

            List<Event> events1=user.getMyEvents();
            events1.add(event);
            user.setMyEvents(events1);
            event.setTicketsAvailable(event.getTicketsAvailable()-1);

        }

    }
    public void deleteCommentById(Event event, long commentId) {
        List<Comment> comments = event.getComments();
        if (comments != null) {
            Iterator<Comment> iterator = comments.iterator();
            while (iterator.hasNext()) {
                Comment comment = iterator.next();
                if (comment.getId() == commentId) {
                    iterator.remove();
                    return;
                }
            }
            int newId = 0;
            for (Comment comment : comments) {
                comment.setId(newId++);
            }
        }
    }
    public Comment findCommentById(Event event, Long id) {
        List<Comment> comments = event.getComments();
        if (comments != null) {
            for (Comment comment : comments) {
                if (comment.getId()==id) {
                    return comment;
                }
            }
        }
        return null;
    }
    public List<Event> findMyevents(){
        List<Event> events1= user.getMyEvents();
        return events1;
    }
}
