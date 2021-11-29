package dto;

import com.online_store.stubs.product.ProductDetailResponse;
import com.sun.istack.NotNull;
import domain.category.Category;
import domain.product.Type;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailDto {

    @NotNull
    private Long id;

    @NotNull
    private String label;

    @NotNull
    private Type type;

    @NotNull
    private Category category;

    private String url;

    private Long weight;

    public ProductDetailDto(ProductDetailResponse productResponse) {
        this.id = productResponse.getId();
        this.label = productResponse.getLabel();
        this.type = Type.valueOf(productResponse.getType().name());
        this.url = productResponse.getUrl();
        this.weight = productResponse.getWeight();
        Category category = new Category();
        category.setId(productResponse.getCategory().getId());
        category.setLabel(productResponse.getCategory().getLabel());
        Category parentCategory = new Category();
        parentCategory.setId(productResponse.getCategory().getIdParent());
        category.setParent(parentCategory);
        this.category = category;
    }
}
