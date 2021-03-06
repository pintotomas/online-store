package service;

import client.ProductClient;
import com.online_store.stubs.product.Category;
import com.online_store.stubs.product.ProductResponse;
import com.online_store.stubs.product.ProductsResponse;
import com.online_store.stubs.product.Type;
import com.online_store.stubs.product.cart.AddProductRequest;
import com.online_store.stubs.product.cart.Cart;
import com.online_store.stubs.product.cart.CartServiceGrpc;
import com.online_store.stubs.product.cart.Status;
import dao.CartDao;
import domain.product.Product;
import domain.product.ProductFactory;
import dto.ProductDetailDto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartServiceImpl extends CartServiceGrpc.CartServiceImplBase {

    private CartDao cartDao;

    private static final Logger logger = Logger.getLogger(CartServiceImpl.class.getName());

    public CartServiceImpl(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    private void sendCartResponse(domain.cart.Cart cart, StreamObserver<Cart> streamObserver) {
        Cart.Builder response = Cart.newBuilder();
        response.setId(cart.getId());
        response.setStatus(Status.valueOf(cart.getStatus().name()));
        ProductsResponse.Builder productsResponse = ProductsResponse.newBuilder();
        for (Product product : cart.getProductList()) {
            Category category = Category.newBuilder().setId(product.getCategory().getId())
                    .setLabel(product.getCategory().getLabel())
                    .setIdParent(product.getCategory().getParentId()).build();
            productsResponse.addProductResponse(
                    ProductResponse.newBuilder()
                            .setId(product.getId())
                            .setCategory(category)
                            .setLabel(product.getLabel())
                            .setType(Type.valueOf(product.getType().name())).build()
            );
        }
        response.setProductsResponse(productsResponse.build());

        streamObserver.onNext(response.build());
        streamObserver.onCompleted();
    }

    /**
     * @param request an empty Proto request
     * @param streamObserver a stream where the Cart proto message will be set
     * @throws StatusRuntimeException when no cart exists or its empty
     */
    @Override
    public void finishCart(com.google.protobuf.Empty request, StreamObserver<Cart> streamObserver) {
        Optional<domain.cart.Cart> optionalCart = cartDao.getPending();
        if (optionalCart.isEmpty()) {
            logger.log(Level.SEVERE, "No active cart found");
            streamObserver.onError(io.grpc.Status.NOT_FOUND.asRuntimeException());
            return;
        }
        domain.cart.Cart cart = optionalCart.get();
        if (cart.isEmpty()) {
            logger.log(Level.INFO, "Cart is empty, cant finish cart");
            streamObserver.onError(io.grpc.Status.ABORTED.asRuntimeException());
            return;
        }
        cart.finish();
        cartDao.save(cart);
        sendCartResponse(cart, streamObserver);
    }

    /**
     * @param request an empty Proto request
     * @param streamObserver a stream where the Cart proto message will be set
     */
    @Override
    public void getCart(com.google.protobuf.Empty request, StreamObserver<Cart> streamObserver) {
        Optional<domain.cart.Cart> optionalCart = cartDao.getPending();
        domain.cart.Cart cart = optionalCart.orElse(cartDao.save(new domain.cart.Cart()));

        sendCartResponse(cart, streamObserver);
    }


    /**
     * @param addProductRequest addProductRequest proto message with the id of the product to add to the cart
     * @param streamObserver a stream where the Cart proto message will be set
     * @throws StatusRuntimeException when a product is not found
     */
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
        ProductDetailDto productDetailDto = productClient.getProduct(id);
        return ProductFactory.createProduct(productDetailDto);

    }



}
