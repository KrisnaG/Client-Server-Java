/**
 * @author Krisna Gusti
 */
package a2.src.main.server;

import a2.src.main.utility.Validation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server class that provides various services to the clients. It listens for incoming client 
 * connections and dispatches requests to the appropriate handler. The server uses a thread 
 * pool to manage concurrent requests.
 */
public class Server {
    private int portNumber;
    private static final ServerClientSessionManager serverClientIdManager = new ServerClientSessionManager();
    private static Logger logger = Logger.getLogger(Server.class.getName());
    
    /**
     * Constructor.
     * @param portNumber Port number for server.
     */
    public Server(final int portNumber) {
        this.portNumber = portNumber;
    }

    /**
     * Checks to see if enough memory is available to a given amount of bytes.
     * There are some reserved bytes to ensure normal operations.
     * @param bytes Number of bytes to be added to memory.
     * @return True if there is enough memory available, otherwise, false.
     */
    public static synchronized boolean isMemoryEnoughAvailable(final long bytes) {
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
            logger.log(Level.INFO, "PORT: {0} OPENED - SERVER READY", this.portNumber);
            while (true) {
                // Listens for incoming client connections
                Socket clientSocket = serverSocket.accept();
                
                // Handle client in a separate thread
                ServerClientHandler clientHandler = new ServerClientHandler(clientSocket, serverClientIdManager);
                executor.submit(clientHandler);
            }
        } catch (IOException error) {
            logger.log(Level.WARNING, error.getMessage());
            System.exit(ServerConstants.EXIT_FAILURE_CONNECTION);
        } finally {
            // Shutdown thread manager
            executor.shutdown();
        }
    }

    /**
     * Entry point for the server application.
     * @param args an array of input arguments. Should contain the port number.
     */
    public static void main(String[] args) {

        // Validate input
        if (!Validation.validateInputs(args, ServerConstants.NUMBER_OF_SERVER_START_INPUTS)) {
            logger.log(Level.WARNING, ServerConstants.SERVER_USAGE);
            System.exit(Validation.EXIT_FAILURE_VALIDATION);
        }
        
        // Extract port number - This should be validated as integer already
        Server server = new Server(Integer.parseInt(args[0]));

        // Start sever
        server.start();
    }
}
