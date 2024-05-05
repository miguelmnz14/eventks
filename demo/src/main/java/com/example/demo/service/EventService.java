package com.example.demo.service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.example.demo.InvalidPriceException;
import com.example.demo.model.Comment;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.server.ResponseStatusException;
import org.apache.commons.io.FileUtils;
@Service
public class EventService {
    @Autowired
    private ImageService imageService;
    @Autowired
    private User user;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    private AtomicLong nextId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, Event> events = new ConcurrentHashMap<>();
    private static final Path IMAGES_FOLDER = Paths.get(System.getProperty("user.dir"), "static");

    public Event findById(long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event save(Event event, MultipartFile imageField) throws IOException {
        if (imageField != null && !imageField.isEmpty()) {
            String originalName = imageField.getOriginalFilename();
            String path = originalName;
            event.setImage(path);
            event.setImageFile(BlobProxy.generateProxy(imageField.getInputStream(), imageField.getSize()));
        } else {
            event.setImage("null");
        }
        double price = event.getPrice();
        if (price <= 0) {
            throw new InvalidPriceException("El precio del evento debe ser mayor que cero.");
        }
        return eventRepository.save(event);
    }

    public void addComment(Event event, Comment comment) {
        if (event.getComments() == null) {
            event.setComments(new ArrayList<>());
        }
        List<Comment> comments = event.getComments();
        comments.add(comment);
        event.setComments(comments);
        eventRepository.save(event);
    }


    public Event edit(Event event, MultipartFile imageField) {
        Optional<Event> optionnalEvent = eventRepository.findById(event.getId());
        if (optionnalEvent.isPresent()) {
            Event existingEvent = optionnalEvent.get();
            if (imageField != null && !imageField.isEmpty()) {
                String path = imageField.getOriginalFilename();
                Blob blob = imageService.convertMultiparttoBlob(imageField);
                existingEvent.setImageFile(blob);
                existingEvent.setImage(path);
            }
            event.setComments(existingEvent.getComments());
            existingEvent.setName(event.getName());
            existingEvent.setId(event.getId());
            existingEvent.setArtists(event.getArtists());
            existingEvent.setUsers(event.getUsers());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setTicketsAvailable(event.getTicketsAvailable());
            existingEvent.setPrice(event.getPrice());
            eventRepository.save(existingEvent);
            existingEvent.setImageFile(event.getImageFile());
            return event;
        } else {
            return null;
        }
    }

    public Event edit1(Event event, Long id, Event aux) {
        String image = aux.getImage();
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event existingEvent = optionalEvent.get();
            existingEvent.setImage(image);
            existingEvent.setComments(aux.getComments());
            existingEvent.setName(event.getName());
            existingEvent.setArtists(event.getArtists());
            existingEvent.setUsers(event.getUsers());
            existingEvent.setDescription(event.getDescription());
            existingEvent.setTicketsAvailable(event.getTicketsAvailable());
            existingEvent.setPrice(event.getPrice());
            existingEvent.setImageFile(aux.getImageFile());
            Event savedEvent = eventRepository.save(existingEvent);
            return savedEvent;
        }
        return null;
    }



    public void delete(Event event) throws IOException {
        eventRepository.delete(event);
        Path directoryPath = Path.of("/src/main/resources/static").resolve(event.getId().toString());
        String username = System.getProperty("user.name");
        imageService.deleteImage1(IMAGES_FOLDER.toString(), event.getId());
    }

    public void buy(long id, HttpServletRequest request) {

        String name=request.getUserPrincipal().getName();
        Optional<User> usuari= userRepository.findByUsername(name);
        User usuario =usuari.get();
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();


            List<Event> mysevents = usuario.getMyEvents();

            if (event.getTicketsAvailable() > 0 && !mysevents.contains(event)) {
                if (event.getUsers() == null) {
                    event.setUsers(new ArrayList<>());
                }


                List<User> users = event.getUsers();
                users.add(usuario);
                event.setUsers(users);

                List<Event> events1 = usuario.getMyEvents();
                events1.add(event);

                usuario.setMyEvents(events1);
                event.setTicketsAvailable(event.getTicketsAvailable() - 1);
                userRepository.save(usuario);
                eventRepository.save(event);

            }
        }

    }

    public void deleteCommentById(Event event, long commentId) {
        List<Comment> comments = event.getComments();


        if (comments != null) {
            Iterator<Comment> iterator = comments.iterator();
            while (iterator.hasNext()) {
                Comment comment = iterator.next();
                if (comment.getId() != commentId) {


                } else {
                    event.removeComment(comment);
                }

            }


        }
    }

    public Comment findCommentById(Event event, Long id) {
        List<Comment> comments = event.getComments();
        if (comments != null) {
            for (Comment comment : comments) {
                if (comment.getId() == id) {
                    return comment;
                }
            }
        }
        return null;
    }

    public List<Event> findMyevents() {
        List<Event> events1 = user.getMyEvents();
        return events1;
    }

    public String sanitizexss(String content) {
        String cleanedHTML = Jsoup.clean(content, Whitelist.basicWithImages());
        return cleanedHTML;
    }

    public void savePdf(MultipartFile pdfFile, Long id) throws IOException {
        String originalName = pdfFile.getOriginalFilename();

        Path folder = IMAGES_FOLDER.resolve(id.toString());

        Files.createDirectories(folder);

        pdfFile.transferTo(folder);

    }

    public void savePdfs(MultipartFile pdfFile, Long id) throws IOException {
        String originalName = pdfFile.getOriginalFilename();

        Path folder = IMAGES_FOLDER.resolve(id.toString());

        Files.createDirectories(folder);


        pdfFile.transferTo(folder);
    }
    public void cleandirectory() throws IOException {
        FileUtils.cleanDirectory(IMAGES_FOLDER.toFile());
    }
    public void cleanMyDirectory(Long id) throws IOException {
        Path myPath= IMAGES_FOLDER.resolve(id.toString());
        FileUtils.cleanDirectory(myPath.toFile());
    }
    public void saveSimple(Event event){
        eventRepository.save(event);
    }
}




