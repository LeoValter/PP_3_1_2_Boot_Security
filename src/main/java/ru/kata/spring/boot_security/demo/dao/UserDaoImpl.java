package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void delete(int id) {
        User user = getById(id);
        entityManager.remove(user);
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> query = entityManager.createQuery("SELECT u from User u", User.class);
        return query.getResultList();
    }

    @Override
    public User getById(Integer id) {
        return entityManager.find(User.class, id);
    }



    @Override
    public User getByUsername(String login) {
        return entityManager.createQuery("select u from User u where u.login = :username", User.class)
                .setParameter("username", login)
                .getResultList().stream()
                .findAny().orElse(null);
    }

    @Override
    @Transactional
    public void update(User user) {
        entityManager.merge(user);
    }
}