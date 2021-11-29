package domain.product;

import dto.ProductDetailDto;
import exceptions.InvalidProductException;

public class ProductFactory {

    private static void setCommonProductAttributes(Product product, ProductDetailDto productDetailDto) {
        product.setId(productDetailDto.getId());
        product.setLabel(productDetailDto.getLabel());
        product.setType(productDetailDto.getType());
        product.setCategory(productDetailDto.getCategory());
    }

    public static Product createProduct(ProductDetailDto productDetailDto) {

        if (productDetailDto.getUrl().isEmpty()) {
            PhysicalProduct physicalProduct = new PhysicalProduct();
            physicalProduct.setWeight(productDetailDto.getWeight());
            setCommonProductAttributes(physicalProduct, productDetailDto);
            return physicalProduct;
        } else if (productDetailDto.getWeight().equals(0L)) {
            DigitalProduct digitalProduct = new DigitalProduct();
            setCommonProductAttributes(digitalProduct, productDetailDto);
            digitalProduct.setUrl(productDetailDto.getUrl());
            return digitalProduct;
        }
        throw new InvalidProductException();
    }
}
