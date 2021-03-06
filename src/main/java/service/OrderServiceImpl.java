package service;

import client.CartClient;
import client.ProductClient;
import com.online_store.stubs.product.Category;
import com.online_store.stubs.product.ProductResponse;
import com.online_store.stubs.product.ProductsResponse;
import com.online_store.stubs.product.Type;
import com.online_store.stubs.product.order.OrderRequest;
import com.online_store.stubs.product.order.OrderResponse;
import com.online_store.stubs.product.order.OrderServiceGrpc;
import dao.OrderDao;
import domain.order.Order;
import domain.product.Product;
import domain.product.ProductFactory;
import dto.CartProductsDto;
import dto.ProductDetailDto;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

    private OrderDao orderDao;

    public OrderServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    private static final Logger logger = Logger.getLogger(OrderServiceImpl.class.getName());


    /**
     * @param orderRequest a orderRequest proto message containing the order id to obtain
     * @param orderResponseStreamObserver a stream where the OrderResponse proto message will be set
     * @throws StatusRuntimeException when the order is not found
     */
    @Override
    public void getOne(OrderRequest orderRequest, StreamObserver<OrderResponse> orderResponseStreamObserver) {
        Long orderId = orderRequest.getId();
        Optional<Order> order = orderDao.findById(orderId);
        if (order.isEmpty()) {
            logger.log(Level.SEVERE, "No order found with id " + orderId);
            orderResponseStreamObserver.onError(Status.NOT_FOUND.asRuntimeException());
            return;
        }
        OrderResponse.Builder orderResponse = OrderResponse.newBuilder();
        ProductsResponse.Builder productsResponse = ProductsResponse.newBuilder();
        for (Product product : order.get().getProductList()) {
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
        orderResponse.setProductsResponse(productsResponse.build());
        orderResponse.setId(order.get().getId());
        orderResponseStreamObserver.onNext(orderResponse.build());
        orderResponseStreamObserver.onCompleted();

    }

    /**
     * @param request an empty Proto request
     * @param orderResponseStreamObserver a stream where the OrderResponse proto message will be set
     */
    @Override
    public void create(com.google.protobuf.Empty request, StreamObserver<OrderResponse> orderResponseStreamObserver) {

        try {
            List<Product> products = obtainProductsFromCart();
            Order order = new Order(products);
            order = orderDao.save(order);
            OrderResponse.Builder orderResponse = OrderResponse.newBuilder();
            ProductsResponse.Builder productsResponse = ProductsResponse.newBuilder();
            for (Product product : order.getProductList()) {
                Category category = Category.newBuilder().setId(product.getCategory().getId())
                        .setLabel(product.getCategory().getLabel())
                        .setIdParent(product.getCategory().getParentId()).build();
                productsResponse.addProductResponse(
                        ProductResponse.newBuilder()
                                .setId(product.getId())
                                .setLabel(product.getLabel())
                                .setCategory(category)
                                .setType(Type.valueOf(product.getType().name())).build()
                );
            }
            orderResponse.setProductsResponse(productsResponse.build());
            orderResponse.setId(order.getId());
            orderResponseStreamObserver.onNext(orderResponse.build());
            orderResponseStreamObserver.onCompleted();
        } catch (StatusRuntimeException statusRuntimeException) {
            logger.log(Level.SEVERE, statusRuntimeException.getLocalizedMessage());
            orderResponseStreamObserver.onError(statusRuntimeException);
        }
    }

    private List<Product> obtainProductsFromCart() {

        ManagedChannel cartClientChannel = ManagedChannelBuilder.forTarget("localhost:8081")
                .usePlaintext()
                .build();

        CartClient cartClient = new CartClient(cartClientChannel);
        CartProductsDto cartProductsDto = cartClient.finishCart();

        ManagedChannel productClientChannel = ManagedChannelBuilder.forTarget("localhost:8080")
                .usePlaintext()
                .build();

        ProductClient productClient = new ProductClient(productClientChannel);
        List<ProductDetailDto> productDetailDtos = productClient.getProducts(cartProductsDto.getProductIds());
        return productDetailDtos.stream().map( productDetailDto ->
                ProductFactory.createProduct(productDetailDto)
        ).collect(Collectors.toList());
    }
}
