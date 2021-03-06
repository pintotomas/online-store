package server;

import dao.DigitalProductDao;
import dao.PhysicalProductDao;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.ProductServiceImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductServer {

    private static final Logger logger = Logger.getLogger(ProductServer.class.getName());

    public static void main(String[] args) {


        DigitalProductDao digitalProductDao = new DigitalProductDao();

        PhysicalProductDao physicalProductDao = new PhysicalProductDao();

        Server server = ServerBuilder.forPort(8080)
                .addService(new ProductServiceImpl(digitalProductDao, physicalProductDao))
                .build();
        try {
            server.start();
            logger.log(Level.INFO, "Product server started on port 8080");
            server.awaitTermination();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Product server did not start");
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Product server shut down or interrupted");
        }
    }
}
