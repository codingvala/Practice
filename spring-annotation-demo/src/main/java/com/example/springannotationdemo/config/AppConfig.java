package com.example.springannotationdemo.config;

import com.example.springannotationdemo.model.User;
import com.example.springannotationdemo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner dataLoader(UserRepository repo) {
        return args -> {
            repo.save(new User("Alice"));
            repo.save(new User("Bob"));
        };
    }
}
