package com.akfc.training.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ActorDao {

    private EntityManagerFactory emf;
    private final EntityManager em;

    public ActorDao() {
        this.emf = Persistence.createEntityManagerFactory("h2-persistence-unit");
        this.em = emf.createEntityManager();
    }

    public Actor create(Actor actor) {
        em.getTransaction().begin();
        em.persist(actor);
        em.getTransaction().commit();
        return actor;
    }

    public Actor findById(Long id) {
        return em.find(Actor.class, id);
    }

    public List<Actor> findAll() {
        return em.createQuery("SELECT a FROM Actor a", Actor.class).getResultList();
    }

    public Actor update(Actor actor) {
        return em.merge(actor);
    }

    public void delete(Actor actor) {
        if (em.contains(actor)) {
            em.remove(actor);
        } else {
            em.remove(em.merge(actor));
        }
    }

    public static void main(String[] args) {
        H2Console h2Console = new H2Console();
        h2Console.start();
        ActorDao actorDao = new ActorDao();
        Actor actor = new Actor();
        actor.setFirstname("John");
        actor.setLastname("Doe");
        Actor a = actorDao.create(actor);
        System.out.println("Actor created: " + a);
        actorDao.findAll().forEach(System.out::println);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            reader.readLine();
            h2Console.join();
            System.out.println("Exiting the application...");
            System.exit(0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class H2Console extends Thread {

        @Override
        public void run() {
            try {
                org.h2.tools.Console.main();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
