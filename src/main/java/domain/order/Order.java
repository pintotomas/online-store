package domain.order;

import domain.cart.Cart;
import domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="product_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(targetEntity=Product.class, fetch= FetchType.EAGER)
    @JoinTable(name = "order_product", joinColumns =
    @JoinColumn(name = "id_order"))
    private List<Product> productList;

    public Order(Cart cart) {
        this.productList = cart.getProductList();
    }

}
