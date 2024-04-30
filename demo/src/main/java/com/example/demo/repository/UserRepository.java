package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
