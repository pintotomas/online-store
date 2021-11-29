package service;

import com.online_store.stubs.product.*;
import dao.DigitalProductDao;
import dao.PhysicalProductDao;
import domain.product.DigitalProduct;
import domain.product.PhysicalProduct;
import domain.product.Product;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductServiceImpl extends ProductServiceGrpc.ProductServiceImplBase {

    private DigitalProductDao digitalProductDao = new DigitalProductDao();

    private PhysicalProductDao physicalProductDao = new PhysicalProductDao();

    private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());

    private void setCommonProductResponse(Product product, ProductDetailResponse.Builder productResponse) {
        productResponse.setId(product.getId());
        productResponse.setLabel(product.getLabel());
        productResponse.setType(Type.valueOf(product.getType().name()));
    }

    @Override
    public void getOneProduct(ProductRequest request, StreamObserver<ProductDetailResponse> responseObserver) {
        Long productId = request.getId();
        ProductDetailResponse.Builder productResponse = ProductDetailResponse.newBuilder();

        Optional<DigitalProduct> optionalDigitalProduct = digitalProductDao.findById(productId);
        Optional<PhysicalProduct> optionalPhysicalProduct = physicalProductDao.findById(productId);
        if (optionalDigitalProduct.isPresent()) {
            DigitalProduct digitalProduct = optionalDigitalProduct.get();
            setCommonProductResponse(digitalProduct, productResponse);
            productResponse.setUrl(digitalProduct.getUrl());
        }
        else if (optionalPhysicalProduct.isPresent()) {
            PhysicalProduct physicalProduct = optionalPhysicalProduct.get();
            setCommonProductResponse(physicalProduct, productResponse);
            productResponse.setWeight(physicalProduct.getWeight());
        }

        if (optionalDigitalProduct.isPresent() || optionalPhysicalProduct.isPresent()) {
            responseObserver.onNext(productResponse.build());
            responseObserver.onCompleted();
        } else {
            logger.log(Level.SEVERE, "No product found with id " + productId);
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getProducts(ProductsRequest request, StreamObserver<ProductsDetailResponse> responseObserver) {
        List<Long> ids = request.getIdList();
        ProductsDetailResponse.Builder productsResponse = ProductsDetailResponse.newBuilder();
        for (Long productId : ids) {
            Optional<DigitalProduct> optionalDigitalProduct = digitalProductDao.findById(productId);
            Optional<PhysicalProduct> optionalPhysicalProduct = physicalProductDao.findById(productId);
            ProductDetailResponse.Builder productResponse = ProductDetailResponse.newBuilder();
            if (optionalDigitalProduct.isPresent()) {
                DigitalProduct digitalProduct = optionalDigitalProduct.get();
                setCommonProductResponse(digitalProduct, productResponse);
                productResponse.setUrl(digitalProduct.getUrl());
                productsResponse.addProductResponse(productResponse);
            }
            else if (optionalPhysicalProduct.isPresent()) {
                PhysicalProduct physicalProduct = optionalPhysicalProduct.get();
                setCommonProductResponse(physicalProduct, productResponse);
                productResponse.setWeight(physicalProduct.getWeight());
                productsResponse.addProductResponse(productResponse);
            }
            else {
                logger.log(Level.SEVERE, "No product found with id " + productId);
                responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
                return;
            }
        }
        responseObserver.onNext(productsResponse.build());
        responseObserver.onCompleted();
    }
}
