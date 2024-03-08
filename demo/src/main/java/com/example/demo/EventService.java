package com.example.demo;
import java.util.List;
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
    public Optional<Event> findById(long id) {
        if(this.events.containsKey(id)) {
            return Optional.of(this.events.get(id));
        }
        return Optional.empty();
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
        long id = event.getId();
        events.put(id, event);
        return event;
    }
    public Event edit1(Event event, Long id){
        events.put(id, event);
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
