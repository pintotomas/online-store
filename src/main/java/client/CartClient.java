package client;

import com.online_store.stubs.product.cart.Cart;
import com.online_store.stubs.product.cart.CartServiceGrpc;
import dto.CartProductsDto;
import io.grpc.Channel;


public class CartClient {

    private CartServiceGrpc.CartServiceBlockingStub cartServiceBlockingStub;

    public CartClient(Channel channel) {
        cartServiceBlockingStub = CartServiceGrpc.newBlockingStub(channel);
    }

    public CartProductsDto finishCart() {
        Cart cart = cartServiceBlockingStub.finishCart(com.google.protobuf.Empty.newBuilder().build());
        return new CartProductsDto(cart);
    }
}
