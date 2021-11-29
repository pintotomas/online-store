package service;

import client.ProductClient;
import com.online_store.stubs.product.*;
import com.online_store.stubs.product.cart.AddProductRequest;
import com.online_store.stubs.product.cart.Cart;
import com.online_store.stubs.product.cart.CartServiceGrpc;
import com.online_store.stubs.product.cart.Status;
import dao.CartDao;
import domain.product.Product;
import domain.product.ProductFactory;
import dto.ProductDto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartServiceImpl extends CartServiceGrpc.CartServiceImplBase {

    private CartDao cartDao = new CartDao();

    private static final Logger logger = Logger.getLogger(CartServiceImpl.class.getName());

    private void sendCartResponse(domain.cart.Cart cart, StreamObserver<Cart> streamObserver) {
        Cart.Builder response = Cart.newBuilder();
        response.setId(cart.getId());
        response.setStatus(Status.valueOf(cart.getStatus().name()));
        ProductsResponse.Builder productsResponse = ProductsResponse.newBuilder();
        for (Product product : cart.getProductList()) {
            productsResponse.addProductResponse(
                    ProductResponse.newBuilder()
                            .setId(product.getId())
                            .setLabel(product.getLabel())
                            .setType(Type.valueOf(product.getType().name())).build()
            );
        }
        response.setProductsResponse(productsResponse.build());

        streamObserver.onNext(response.build());
        streamObserver.onCompleted();
    }
    @Override
    public void getCart(com.google.protobuf.Empty request, StreamObserver<Cart> streamObserver) {
        Optional<domain.cart.Cart> optionalCart = cartDao.getPending();
        domain.cart.Cart cart = optionalCart.orElse(cartDao.save(new domain.cart.Cart()));

        sendCartResponse(cart, streamObserver);
    }


    @Override
    public void addOneProduct(AddProductRequest addProductRequest, StreamObserver<Cart> streamObserver) {
        Optional<domain.cart.Cart> optionalCart = cartDao.getPending();
        domain.cart.Cart cart = optionalCart.orElse(cartDao.save(new domain.cart.Cart()));
        try {

        Product product = obtainProduct(addProductRequest.getId());
        if (cart.contains(product)) {
            logger.log(Level.SEVERE, "Cart already contains product");
            streamObserver.onError(io.grpc.Status.ABORTED.asRuntimeException());
            return;
        }
        cart.addProduct(product);
        cart = cartDao.save(cart);

        sendCartResponse(cart, streamObserver);

        } catch (StatusRuntimeException statusRuntimeException) {
            logger.log(Level.SEVERE, statusRuntimeException.getLocalizedMessage());
            streamObserver.onError(statusRuntimeException);
        }
    }

    private Product obtainProduct(Long id) {

        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext()
                .build();

        ProductClient productClient = new ProductClient(channel);
        ProductDto productDto = productClient.getProduct(id);
        return ProductFactory.createProduct(productDto);

    }



}
