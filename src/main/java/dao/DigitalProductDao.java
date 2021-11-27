package dao;

import domain.product.DigitalProduct;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

public class DigitalProductDao {
    public Optional<DigitalProduct> findById(Long id){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("online-cart");
        EntityManager em = emf.createEntityManager();

        // We can find a record in the database for a given id using the find method.
        // for the find method we have to provide our entity class and the id.
        return Optional.ofNullable(em.find(DigitalProduct.class, id));
    }
}
