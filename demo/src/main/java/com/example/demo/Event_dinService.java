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
    public List<Event> findAll(String artist,Double price) {
        String query = "SELECT e FROM Event e";
        boolean added = false;
        if (!artist.isEmpty()) {
            query += " WHERE e.artists = :artist";
            added = true;
        }

        if (price != null){
            if (added){
                query += " AND e.price <= :price";
            } else {
                query += " WHERE e.price <= :price";
            }
        }

        TypedQuery<Event> typedQuery = entityManager.createQuery(query, Event.class);
        if (!artist.isEmpty()) {
            typedQuery.setParameter("artist", artist);
        }
        if (price != null){
            typedQuery.setParameter("price", price);
        }
        return typedQuery.getResultList();
    }

}
