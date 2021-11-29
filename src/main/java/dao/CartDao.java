package dao;

import domain.cart.Cart;
import domain.cart.Status;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

public class CartDao {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("online-cart");
    EntityManager em = emf.createEntityManager();

    /**
     * @return A cart if one is found with status PENDING
     */
    public Optional<Cart> getPending() {
        //Returns current cart with pending status
        Status status = Status.PENDING;
        List<Cart> carts = em.createQuery("SELECT c FROM Cart c WHERE c.status = :status", Cart.class)
                .setParameter("status", status)
                .getResultList();
        if (carts.isEmpty()) return Optional.empty();
        else return Optional.ofNullable(carts.get(0));
    }

    /**
     * @param cart A Cart object
     * @return The saved Cart
     */
    public Cart save(Cart cart) {
        em.getTransaction().begin();
        if (!em.contains(cart)) {
            em.persist(cart);
            em.flush();
        }
        em.getTransaction().commit();
        return getPending().get();
    }
}
