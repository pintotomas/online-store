package client;

import com.online_store.stubs.product.*;
import dto.ProductDetailDto;
import io.grpc.Channel;

import java.util.List;
import java.util.stream.Collectors;

public class ProductClient {

    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ProductClient(Channel channel){
        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);

    }

    public ProductDetailDto getProduct(Long productId) {
        ProductRequest productRequest = ProductRequest.newBuilder().setId(productId).build();
        ProductDetailResponse productResponse = productServiceBlockingStub.getOneProduct(productRequest);

        return new ProductDetailDto(productResponse);
    }

    public List<ProductDetailDto> getProducts(List<Long> productIds) {
        ProductsRequest productsRequest = ProductsRequest.newBuilder().addAllId(productIds).build();
        ProductsDetailResponse productsDetailResponse = productServiceBlockingStub.getProducts(productsRequest);
        return productsDetailResponse.getProductResponseList().stream().map(productDetailResponse ->
                new ProductDetailDto(productDetailResponse)).collect(Collectors.toList());
    }
}
