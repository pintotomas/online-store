package service;

import com.online_store.stubs.product.ProductDetailResponse;
import com.online_store.stubs.product.ProductRequest;
import com.online_store.stubs.product.ProductsDetailResponse;
import com.online_store.stubs.product.ProductsRequest;
import dao.DigitalProductDao;
import dao.PhysicalProductDao;
import domain.category.Category;
import domain.product.DigitalProduct;
import domain.product.PhysicalProduct;
import domain.product.Type;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Assertions;
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
public class ProductServiceTest {

    private ProductServiceImpl productService;

    private DigitalProductDao digitalProductDao;

    private PhysicalProductDao physicalProductDao;

    @Captor
    private ArgumentCaptor<ProductDetailResponse> productDetailResponseArgumentCaptor = ArgumentCaptor.forClass(
            ProductDetailResponse.class
    );

    @Captor
    private ArgumentCaptor<ProductsDetailResponse> productsDetailResponseArgumentCaptor = ArgumentCaptor.forClass(
            ProductsDetailResponse.class
    );

    @Captor
    private ArgumentCaptor<Throwable> throwableArgumentCaptor = ArgumentCaptor.forClass(
            Throwable.class
    );


    @BeforeAll
    public void setUp() {
        digitalProductDao = Mockito.mock(DigitalProductDao.class);
        physicalProductDao = Mockito.mock(PhysicalProductDao.class);
        productService = new ProductServiceImpl(digitalProductDao, physicalProductDao);
    }

    @Test
    public void whenFindOneProductAndProductNotFoundThenSetErrorResponse() {
        when(digitalProductDao.findById(1L)).thenReturn(Optional.empty());
        when(physicalProductDao.findById(1L)).thenReturn(Optional.empty());
        ProductRequest productRequest = ProductRequest.newBuilder().setId(1L).build();
        StreamObserver<ProductDetailResponse> responseObserver = Mockito.mock(StreamObserver.class);
        productService.getOneProduct(productRequest, responseObserver);
        verify(responseObserver, times(1)).onError(throwableArgumentCaptor.capture());
    }

    @Test
    public void whenFindOneProductAndProductFoundThenSetAndCallOnNextAndOnComplete() {
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
        when(digitalProductDao.findById(1L)).thenReturn(Optional.of(digitalProduct));
        when(physicalProductDao.findById(1L)).thenReturn(Optional.empty());
        ProductRequest productRequest = ProductRequest.newBuilder().setId(1L).build();
        StreamObserver<ProductDetailResponse> responseObserver = Mockito.mock(StreamObserver.class);
        productService.getOneProduct(productRequest, responseObserver);
        verify(responseObserver, times(1)).onCompleted();
        verify(responseObserver, times(1)).onNext(productDetailResponseArgumentCaptor.capture());
        Assertions.assertEquals(1L, productDetailResponseArgumentCaptor.getValue().getId());
        Assertions.assertEquals("label", productDetailResponseArgumentCaptor.getValue().getLabel());
    }

    @Test
    public void whenFindProductsAndAnyProductNotFoundThenSetErrorResponse() {
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
        when(digitalProductDao.findById(1L)).thenReturn(Optional.of(digitalProduct));
        when(physicalProductDao.findById(1L)).thenReturn(Optional.empty());
        when(digitalProductDao.findById(2L)).thenReturn(Optional.empty());
        when(physicalProductDao.findById(2L)).thenReturn(Optional.empty());

        ProductsRequest.Builder productRequest = ProductsRequest.newBuilder();
        productRequest.addAllId(Arrays.asList(1L, 2L));
        StreamObserver<ProductsDetailResponse> responseObserver = Mockito.mock(StreamObserver.class);
        productService.getProducts(productRequest.build(), responseObserver);
        verify(responseObserver, times(1)).onError(throwableArgumentCaptor.capture());
    }

    @Test
    public void whenFindProductsAndFindAllThenSetAndCallOnNextAndComplete() {
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

        PhysicalProduct physicalProduct = new PhysicalProduct();
        physicalProduct.setId(2L);
        physicalProduct.setWeight(1L);
        physicalProduct.setLabel("label physical");
        physicalProduct.setCategory(category);
        physicalProduct.setType(Type.SHOPPING_GOODS);
        when(digitalProductDao.findById(1L)).thenReturn(Optional.of(digitalProduct));
        when(physicalProductDao.findById(1L)).thenReturn(Optional.empty());
        when(digitalProductDao.findById(2L)).thenReturn(Optional.empty());
        when(physicalProductDao.findById(2L)).thenReturn(Optional.of(physicalProduct));

        ProductsRequest.Builder productRequest = ProductsRequest.newBuilder();
        productRequest.addAllId(Arrays.asList(1L, 2L));
        StreamObserver<ProductsDetailResponse> responseObserver = Mockito.mock(StreamObserver.class);
        productService.getProducts(productRequest.build(), responseObserver);

        verify(responseObserver, times(1)).onCompleted();
        verify(responseObserver, times(1)).onNext(productsDetailResponseArgumentCaptor.capture());
        Assertions.assertEquals(1L, productsDetailResponseArgumentCaptor.getValue().getProductResponse(0).getId());
        Assertions.assertEquals("label", productsDetailResponseArgumentCaptor.getValue().getProductResponse(0).getLabel());
        Assertions.assertEquals(2L, productsDetailResponseArgumentCaptor.getValue().getProductResponse(1).getId());
        Assertions.assertEquals("label physical", productsDetailResponseArgumentCaptor.getValue().getProductResponse(1).getLabel());
    }

}
