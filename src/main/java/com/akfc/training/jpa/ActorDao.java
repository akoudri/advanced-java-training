package com.akfc.training.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.h2.tools.Server;

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

    public static void main(String[] args) throws InterruptedException {
        ActorDao actorDao = new ActorDao();
        Actor actor = new Actor();
        actor.setFirstname("John");
        actor.setLastname("Doe");
        Actor a = actorDao.create(actor);
        System.out.println("Actor created: " + a);
        actorDao.findAll().forEach(System.out::println);
        H2Console h2Console = new H2Console();
        h2Console.start();
        h2Console.join();
    }

    static class H2Console extends Thread {
        @Override
        public void run() {
            Server server = null;
            try {
                // Start the H2 Console server
                server = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082");
                server.start();
                System.out.println("H2 Console started at http://localhost:8082");
                System.out.println("Press any key to stop the server and exit...");

                // Wait for any key press
                Scanner scanner = new Scanner(System.in);
                scanner.nextLine();

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Stop the H2 Console server
                if (server != null) {
                    server.stop();
                    System.out.println("H2 Console server stopped.");
                }
            }
        }
    }
}
