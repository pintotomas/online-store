package domain.product;

import dto.ProductDto;
import exceptions.InvalidProductException;

public class ProductFactory {

    private static void setCommonProductAttributes(Product product, ProductDto productDto) {
        product.setId(productDto.getId());
        product.setLabel(productDto.getLabel());
        product.setType(productDto.getType());
    }

    public static Product createProduct(ProductDto productDto) {

        if (productDto.getUrl().isEmpty()) {
            PhysicalProduct physicalProduct = new PhysicalProduct();
            physicalProduct.setWeight(productDto.getWeight());
            setCommonProductAttributes(physicalProduct, productDto);
            return physicalProduct;
        } else if (productDto.getWeight().equals(0L)) {
            DigitalProduct digitalProduct = new DigitalProduct();
            setCommonProductAttributes(digitalProduct, productDto);
            digitalProduct.setUrl(productDto.getUrl());
            return digitalProduct;
        }
        throw new InvalidProductException();
    }
}
