package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {
    void add(User user);
    void delete(int id);
    List<User> getAll();

    User getById(Integer id);

    void update(User user);

    User getByUsername(String login);
}
