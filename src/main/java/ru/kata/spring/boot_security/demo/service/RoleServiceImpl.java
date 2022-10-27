package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public boolean add(Role role) {
        Role roleFromDb = roleDao.getByName(role.getName());
        if (roleFromDb != null) {
            return false;
        }
        roleDao.add(role);
        return true;
    }

    @Override
    public List<Role> getAll() {
        return roleDao.getAll();
    }
}
