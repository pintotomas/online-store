package dao;

import domain.order.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

public class OrderDao {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("online-cart");
    EntityManager em = emf.createEntityManager();

    public Order save(Order order) {
        em.getTransaction().begin();
        if (!em.contains(order)) {
            em.persist(order);
            em.flush();
        }
        em.getTransaction().commit();
        return order;
    }

    public Optional<Order> findById(Long id) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("online-cart");
        EntityManager em = emf.createEntityManager();

        return Optional.ofNullable(em.find(Order.class, id));
    }
}
