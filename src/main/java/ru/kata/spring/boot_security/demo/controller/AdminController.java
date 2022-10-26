package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @ModelAttribute(name = "user")
    public User user() {
        return new User();
    }

    @GetMapping
    public String showUsers(Model model, Principal principal) {
        System.out.println(principal);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("roles", roleService.getAll());
        return "users";
    }

    @PostMapping("/adduser")
    public String addUser(User user) {
        System.out.println(user);
        userService.add(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(User user) {
        System.out.println(user);
        userService.update(user);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
