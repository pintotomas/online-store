package service;

import com.online_store.stubs.product.cart.Cart;
import dao.CartDao;
import domain.category.Category;
import domain.product.DigitalProduct;
import domain.product.Type;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartServiceTest {

    private CartServiceImpl cartService;

    private CartDao cartDao;

    @Captor
    private ArgumentCaptor<Throwable> throwableArgumentCaptor = ArgumentCaptor.forClass(
            Throwable.class
    );

    @Captor private ArgumentCaptor<domain.cart.Cart> cartArgumentCaptor = ArgumentCaptor.forClass(
            domain.cart.Cart.class
    );;

    @BeforeAll
    public void setUp() {
        cartDao = Mockito.mock(CartDao.class);
        cartService = new CartServiceImpl(cartDao);
    }

    @Test
    public void whenFinishCartWhenNoCartExistsThenCallOnError() {
        when(cartDao.getPending()).thenReturn(Optional.empty());
        com.google.protobuf.Empty request = com.google.protobuf.Empty.newBuilder().build();
        StreamObserver<Cart> streamObserver = Mockito.mock(StreamObserver.class);
        cartService.finishCart(request, streamObserver);
        verify(streamObserver, times(1)).onError(throwableArgumentCaptor.capture());
    }

    @Test
    public void whenFinishCartWhenCartIsEmptyThenCallOnError() {
        domain.cart.Cart cart = new domain.cart.Cart();
        cart.setProductList(Arrays.asList());
        when(cartDao.getPending()).thenReturn(Optional.of(cart));
        com.google.protobuf.Empty request = com.google.protobuf.Empty.newBuilder().build();
        StreamObserver<Cart> streamObserver = Mockito.mock(StreamObserver.class);
        cartService.finishCart(request, streamObserver);
        verify(streamObserver, times(1)).onError(throwableArgumentCaptor.capture());
    }

    @Test
    public void whenFinishCartWhenCartHasProductsThenChangeStatusAndSave() {
        domain.cart.Cart cart = new domain.cart.Cart();
        cart.setId(1L);
        DigitalProduct digitalProduct = new DigitalProduct();
        digitalProduct.setId(1L);
        digitalProduct.setUrl("url");
        digitalProduct.setLabel("label");
        Category category = new Category();
        category.setId(1L);
        category.setParent(null);
        category.setLabel("label");
        digitalProduct.setCategory(category);
        digitalProduct.setType(Type.CONVENIENCE_GOODS);
        cart.setProductList(Arrays.asList(digitalProduct));
        when(cartDao.getPending()).thenReturn(Optional.of(cart));
        com.google.protobuf.Empty request = com.google.protobuf.Empty.newBuilder().build();
        StreamObserver<Cart> streamObserver = Mockito.mock(StreamObserver.class);
        cartService.finishCart(request, streamObserver);
        verify(cartDao, times(1)).save(cartArgumentCaptor.capture());
    }
}
