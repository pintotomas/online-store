package domain.cart;

import com.sun.istack.NotNull;
import domain.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Entity
@Data
@Table(name = "cart")
public class Cart implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(targetEntity=Product.class, fetch= FetchType.EAGER)
    @JoinTable(name = "cart_product", joinColumns =
    @JoinColumn(name = "id_cart"),
            inverseJoinColumns = @JoinColumn(name = "id_product"))
    private List<Product> productList;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public Cart() {
        this.status = Status.PENDING;
        this.productList = new ArrayList<>();
    }

    public void addProduct(Product product) {
        this.productList.add(product);
    }

    public boolean contains(Product product) {
        return productList.contains(product);
    }
}
