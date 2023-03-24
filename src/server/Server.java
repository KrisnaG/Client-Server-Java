/**
 * 
 */
package a2.src.server;

import a2.src.utility.Validation;
import a2.src.utility.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 */
public class Server {
    private int portNumber;
    private final static ClientIdManager clientIdManager = new ClientIdManager();
    
    /**
     * 
     * @param portNumber
     */
    public Server(final int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static boolean isMemoryEnoughAvailable(long bytes) {
        return Runtime.getRuntime().freeMemory() + bytes + Constants.SERVER_RESERVED_BYTES
                < Runtime.getRuntime().maxMemory();
    }

    /**
     * 
     */
    public void start() {
         // Executor to manage threads
         ExecutorService executor = Executors.newFixedThreadPool(Constants.MAX_THREAD_COUNT);
 
         // Attempts to open port
         try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
             while (true) {
                 try {
                     // Listens for incoming client connections
                     Socket clientSocket = serverSocket.accept();
 
                      // Handle client in a separate thread
                     ClientHandler clientHandler = new ClientHandler(clientSocket, clientIdManager);
                     executor.execute(clientHandler);
                 } catch (IOException error) {
                     System.err.println("Error accepting client connection");
                     System.err.println(error.getMessage());
                     System.exit(Constants.EXIT_FAILURE_CONNECTION);
                 }
             }
         } catch (IOException error) {
             System.err.println("Could not start server on port " + portNumber);
             System.err.println(error.getMessage());
             System.exit(Constants.EXIT_FAILURE_CONNECTION);
         }
 
         // TODO: Add hook?
         executor.shutdown();
    }

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        // Validate input
        Validation.validateInputs(args, Constants.NUMBER_OF_SERVER_START_INPUTS);
        
        // Extract port number - This should be validated as integer already
        Server server = new Server(Integer.parseInt(args[0]));

        // Start sever
        server.start();
    }
}
