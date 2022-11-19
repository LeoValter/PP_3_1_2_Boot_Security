package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceIml implements UserService {

    private UserDao userDao;

    private RoleDao roleDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public boolean add(User user) {
        User userFromDb = userDao.getByUsername(user.getLogin());
        if (userFromDb != null) {
            return false;
        }

        System.out.println("Roles for Add before preparing: " + user.getRoles());

        user.setRoles(prepareRoleToSave(user.getRoles()));
        System.out.println("Roles for Add after preparing: " + user.getRoles());
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        userDao.add(user);
        return true;
    }

    @Override
    public User get(Integer id) {
        return userDao.getById(id);
    }

    @Override
    public void update(User user) {
        System.out.println("User for Update before null check: " + user);
        System.out.println("Roles for Update before null check: " + user.getRoles());

        if (user.getRoles() == null) {
            Set<Role> roles = new HashSet<>();
            user.setRoles(roles);
        }

        System.out.println("Roles for Update after null check: " + user.getRoles());

        System.out.println("Roles for Update before preparing: " + user.getRoles());

        if (user.getRoles().isEmpty()) {
            User updatingUser = userDao.getById(user.getId());
            user.setRoles(updatingUser.getRoles());
        } else {
            user.setRoles(prepareRoleToSave(user.getRoles()));
        }
        System.out.println("Roles for Update after preparing: " + user.getRoles());
        userDao.update(user);
    }

    @Override
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User getByLogin(String login) {
        return userDao.getByUsername(login);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s not found", username));
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesRoAuthorities(user.getRoles()));
    }

    private Set<Role> prepareRoleToSave(Set<Role> roles) {

        if (roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(new Role("ROLE_USER"));
        }

        List<String> roleNames = roles.stream()
                .map(Role::getName).collect(Collectors.toList());

        Set<Role> result = new HashSet<>();
        for (String roleName : roleNames) {
            result.add(roleDao.getByName(roleName));
        }

        return result;
    }

    private Collection<? extends GrantedAuthority> mapRolesRoAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
