package com.example.demo;
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



        long id = nextId.getAndIncrement();
        event.setId(id);
        events.put(id, event);
        return event;
    }
    public void addComment(Event event, Comment comment){
        if (event.getComments() == null) {
            event.setComments(new ArrayList<>());
        }
        List<Comment> comments = event.getComments();
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
    public Event edit1(Event event, Long id){
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
}
