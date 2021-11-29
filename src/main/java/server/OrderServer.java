package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.OrderServiceImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderServer {

    private static final Logger logger = Logger.getLogger(OrderServer.class.getName());

    public static void main(String[] args) {

        Server server = ServerBuilder.forPort(8082)
                .addService(new OrderServiceImpl())
                .build();
        try {
            server.start();
            logger.log(Level.INFO, "Order server started on port 8082");
            server.awaitTermination();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Order server did not start");
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Order server shut down or interrupted");
        }
    }
}
