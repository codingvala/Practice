package com.example.springannotationdemo.repository;

import com.example.springannotationdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
