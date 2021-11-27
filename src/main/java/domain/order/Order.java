package domain.order;

import domain.cart.Cart;
import domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Order {
    @Id
    private Long id;

    @OneToMany(targetEntity=Product.class, fetch= FetchType.EAGER)
    @JoinTable(name = "order_product", joinColumns =
    @JoinColumn(name = "id_order"))
    private List<Product> productList;

    public Order(Cart cart) {
        this.productList = cart.getProductList();
    }

}
