package domain.category;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Category {

    @Id
    private Long id;

    @NotNull
    private String label;

    @OneToOne
    private Category parent;
}
