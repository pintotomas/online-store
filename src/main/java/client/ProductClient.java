package client;

import com.online_store.stubs.product.ProductDetailResponse;
import com.online_store.stubs.product.ProductRequest;
import com.online_store.stubs.product.ProductResponse;
import com.online_store.stubs.product.ProductServiceGrpc;
import dto.ProductDto;
import io.grpc.Channel;

public class ProductClient {

    private ProductServiceGrpc.ProductServiceBlockingStub productServiceBlockingStub;

    public ProductClient(Channel channel){
        productServiceBlockingStub = ProductServiceGrpc.newBlockingStub(channel);

    }

    public ProductDto getProduct(Long productId) {
        ProductRequest productRequest = ProductRequest.newBuilder().setId(productId).build();
        ProductDetailResponse productResponse = productServiceBlockingStub.getOneProduct(productRequest);

        // Send it to the caller, in an appropriate manner in this case as a list.
        return new ProductDto(productResponse);
    }
}
