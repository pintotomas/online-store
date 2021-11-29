package dto;

import com.online_store.stubs.product.cart.Cart;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CartProductsDto {

    private List<Long> productIds;

    public CartProductsDto(Cart cart) {
        this.productIds = cart.getProductsResponse().getProductResponseList().stream().map(
                productResponse -> productResponse.getId()
        ).collect(Collectors.toList());
    }
}
