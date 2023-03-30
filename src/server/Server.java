/**
 * @author Krisna Gusti
 */
package a2.src.server;

import a2.src.utility.Validation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Serve class that provides various services to the clients. It listens for incoming client 
 * connections and dispatches requests to the appropriate handler. The server uses a thread 
 * pool to manage concurrent requests.
 */
public class Server {
    private int portNumber;
    private final static ServerClientSessionManager serverClientIdManager = new ServerClientSessionManager();
    
    /**
     * Constructor.
     * @param portNumber Port number for server.
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
        return Runtime.getRuntime().freeMemory() + bytes + ServerConstants.SERVER_RESERVED_BYTES
                < Runtime.getRuntime().maxMemory();
    }

    /**
     * Starts the server and listen for connections.
     */
    public void start() {
        // Executor to manage threads
        ExecutorService executor = Executors.newFixedThreadPool(ServerConstants.MAX_THREAD_COUNT);
 
        // Attempts to open port
        try (ServerSocket serverSocket = new ServerSocket(this.portNumber)) {
            System.out.printf("Port %d opened. Server Ready\n", this.portNumber);
            while (true) {
                try {
                    // Listens for incoming client connections
                    Socket clientSocket = serverSocket.accept();
                      // Handle client in a separate thread
                    ServerClientHandler clientHandler = new ServerClientHandler(clientSocket, serverClientIdManager);
                    executor.execute(clientHandler);
                } catch (IOException error) {
                    System.err.println("Error accepting client connection");
                    System.err.println(error.getMessage());
                    System.exit(ServerConstants.EXIT_FAILURE_CONNECTION);
                }
            }
        } catch (IOException error) {
            System.err.println(error.getMessage());
            System.exit(ServerConstants.EXIT_FAILURE_CONNECTION);
        } finally {
            // Shutdown thread manager
            executor.shutdown();
        }
    }

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        // Validate input
        if (!Validation.validateInputs(args, ServerConstants.NUMBER_OF_SERVER_START_INPUTS)) {
            System.out.println(ServerConstants.SERVER_USAGE);
            System.exit(Validation.EXIT_FAILURE_VALIDATION);
        }
        
        // Extract port number - This should be validated as integer already
        Server server = new Server(Integer.parseInt(args[0]));

        // Start sever
        server.start();
    }
}
