package domain.product;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotNull
    private String label;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;
}
