package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService service;

    @ModelAttribute(name = "user")
    public User user() {
        return new User();
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @PostMapping("/adduser")
    public String addUser(User user) {
        System.out.println(user);
        service.add(user);
        return "redirect:/";
    }

    @PostMapping("/update")
    public String updateUser(User user) {
        System.out.println(user);
        service.update(user);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        service.delete(id);
        return "redirect:/";
    }
}
