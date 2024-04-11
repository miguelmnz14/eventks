package com.example.demo;

import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import java.util.List;

@Service
public class Event_dinService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EntityManager entityManager;
    public List<Event> findAll(){
        return eventRepository.findAll();
    }
    public List<Event> findAll(String artist,double price) {
        String query = "SELECT e FROM Event e";
        if (!artist.isEmpty()) {
            query += " WHERE e.artists = :artist";
        }

        query += " AND e.price < :price";

        TypedQuery<Event> typedQuery = entityManager.createQuery(query, Event.class);
        if (!artist.isEmpty()) {
            typedQuery.setParameter("artist", artist);
        }
        typedQuery.setParameter("price",price);
        return typedQuery.getResultList();
    }

}
