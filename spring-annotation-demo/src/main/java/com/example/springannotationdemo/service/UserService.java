package com.example.springannotationdemo.service;

import com.example.springannotationdemo.model.User;
import com.example.springannotationdemo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() { return userRepository.findAll(); }
    public User save(User u) { return userRepository.save(u); }
}
