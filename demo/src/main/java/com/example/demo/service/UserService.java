package com.example.demo.service;

import com.example.demo.model.Comment;
import com.example.demo.model.Event;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CommentRepository commentRepository;
    public User findbyId (Long id){
        Optional <User> user = userRepository.findById(id);
        return user.orElse(null);
    }
    public User findbyusername(String username){
        Optional<User> user=userRepository.findByUsername(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            // Handle the case where the user does not exist
            return null;

        }
    }
    public void saveUserinDB(User user){
        userRepository.save(user);
    }
    public List<User> findAllUser(){
        List<User> allusers=new ArrayList<>();
        allusers=userRepository.findAll();
        return  allusers;
    }
    public void deleteUser(String username){
        User userToDelete =findbyusername(username);
        List<User> users=findAllUser();
        if (users.contains(userToDelete)){
            List<Event> myEvents=userToDelete.getMyEvents();
            for (Event event: myEvents){
                List<User> users1=event.getUsers();
                users1.remove(userToDelete);
                eventRepository.save(event);
            }
            List<Comment> comments=commentRepository.findAll();
            for (Comment comment:comments){
                if (comment.getUser().getUsername().equals(username)){
                    commentRepository.delete(comment);
                }
            }
            userRepository.delete(userToDelete);
        }
    }
    public void editUser(String oldUser,String username,String password){
        if (!username.isEmpty()) {
            User user = findbyusername(oldUser);
            user.setUsername(username);
            user.setEncodedPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }
    }

    public boolean existname(String username){
        Optional <User> user = userRepository.findByUsername(username);
        if (user.isPresent()){
            return true;
        } else {
            return false;
        }
    }

    public boolean haveEvent(User user, Event event){
        List <Event> events = user.getMyEvents();
        boolean find = false;
        for(Event event1 : events){
            if (event == event1){
                find = true;
                break;
            }
        }
        return find;
    }

    public boolean checkPassword(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$";
        return password.matches(regex);
    }

}
