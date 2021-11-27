package domain.cart;

import com.sun.istack.NotNull;
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
public class Cart {
    @Id
    private Long id;

    @OneToMany(targetEntity=Product.class, fetch= FetchType.EAGER)
    @JoinTable(name = "cart_product", joinColumns =
    @JoinColumn(name = "id_cart"))
    private List<Product> productList;

    @NotNull
    private Status status;
}
