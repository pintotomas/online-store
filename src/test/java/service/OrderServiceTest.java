package service;

import com.online_store.stubs.product.order.OrderRequest;
import com.online_store.stubs.product.order.OrderResponse;
import dao.OrderDao;
import domain.order.Order;
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
public class OrderServiceTest {

    private OrderServiceImpl orderService;

    private OrderDao orderDao;

    @Captor
    private ArgumentCaptor<Throwable> throwableArgumentCaptor = ArgumentCaptor.forClass(
            Throwable.class
    );

    @Captor
    private ArgumentCaptor<OrderResponse> orderResponseArgumentCaptor = ArgumentCaptor.forClass(
            OrderResponse.class
    );

    @BeforeAll
    public void setUp() {
        orderDao = Mockito.mock(OrderDao.class);
        orderService = new OrderServiceImpl(orderDao);
    }

    @Test
    public void whenFindOneOrderAndOrderNotFoundThenSetErrorResponse() {
        when(orderDao.findById(1L)).thenReturn(Optional.empty());
        OrderRequest orderRequest = OrderRequest.newBuilder().setId(1L).build();
        StreamObserver<OrderResponse> responseObserver = Mockito.mock(StreamObserver.class);
        orderService.getOne(orderRequest, responseObserver);
        verify(responseObserver, times(1)).onError(throwableArgumentCaptor.capture());
    }

    @Test
    public void whenFindOneOrderAndOrderFoundThenCallOnNextAndOnCompleted() {
        Order order = new Order();
        order.setId(1L);
        order.setProductList(Arrays.asList());
        when(orderDao.findById(1L)).thenReturn(Optional.of(order));
        OrderRequest orderRequest = OrderRequest.newBuilder().setId(1L).build();
        StreamObserver<OrderResponse> responseObserver = Mockito.mock(StreamObserver.class);
        orderService.getOne(orderRequest, responseObserver);
        verify(responseObserver, times(1)).onNext(orderResponseArgumentCaptor.capture());
        verify(responseObserver, times(1)).onCompleted();
    }
}
