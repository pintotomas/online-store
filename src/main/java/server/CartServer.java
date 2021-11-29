package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.CartServiceImpl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartServer {

    private static final Logger logger = Logger.getLogger(CartServer.class.getName());

    public static void main(String[] args) {

        Server server = ServerBuilder.forPort(8081)
                .addService(new CartServiceImpl())
                .build();
        try {
            server.start();
            logger.log(Level.INFO, "Cart server started on port 8081");
            server.awaitTermination();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cart server did not start");
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Cart server shut down or interrupted");
        }
    }
}
