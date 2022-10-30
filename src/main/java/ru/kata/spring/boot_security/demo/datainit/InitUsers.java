package ru.kata.spring.boot_security.demo.datainit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitUsers {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public InitUsers(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        Role role1 = new Role("ROLE_ADMIN");
        Role role2 = new Role("ROLE_USER");

        roleService.add(role1);
        roleService.add(role2);

        Set<Role> roleAdmin = new HashSet<>();
        Set<Role> roleUser = new HashSet<>();

        roleAdmin.add(role1);
        roleUser.add(role2);

        User admin = new User("AdminFn", "AdminLn", "admin@mail.ru", "admin", 55, roleAdmin);
        User user = new User("UserFn", "UserLn", "user@mail.ru", "user", 39, roleUser);

        userService.add(admin);
        userService.add(user);
    }
}
