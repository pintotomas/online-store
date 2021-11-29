package dto;

import com.online_store.stubs.product.ProductDetailResponse;
import com.sun.istack.NotNull;
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

    private String url;

    private Long weight;

    public ProductDetailDto(ProductDetailResponse productResponse) {
        this.id = productResponse.getId();
        this.label = productResponse.getLabel();
        this.type = Type.valueOf(productResponse.getType().name());
        this.url = productResponse.getUrl();
        this.weight = productResponse.getWeight();
    }
}
