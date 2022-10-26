package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean add(User user);

    User get(Integer id);

    void update(User user);

    void delete(int id);

    List<User> getAll();

    User getByLogin(String login);
}

