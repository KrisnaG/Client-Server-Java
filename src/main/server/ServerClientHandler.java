/**
 * @author Krisna Gusti
 */
package a2.src.main.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import a2.src.main.utility.Commands;

/**
 * The ServerClientHandler class handles the communication with a single client connected to the server.
 * It extends the Thread class, allowing the handler to be run on a separate thread from the server.
 * 
 * The class receives messages from the client and executes commands based on the message content.
 * The following commands are supported:
 * - CONNECT: Connects the client to the server.
 * - DELETE: Deletes data associated with a given key for the client.
 * - DISCONNECT: Disconnects the client from the server.
 * - GET: Retrieves data associated with a given key for the client.
 * - PUT: Stores data associated with a given key for the client.
 */
public class ServerClientHandler implements Runnable {
    private Socket socket;
    private final ServerClientSessionManager serverClientSessionManager;
    private boolean clientConnected;
    private String clientID;
    private static Logger logger = Logger.getLogger(ServerClientHandler.class.getName());

    /**
     * Constructor.
     * @param socket The socket associated with the client connection.
     * @param serverClientSessionManager The session manager for handling the client's session.
     */
    public ServerClientHandler(Socket socket, ServerClientSessionManager serverClientSessionManager) {
        this.socket = socket;
        this.serverClientSessionManager = serverClientSessionManager;
        this.clientID = null;
        this.clientConnected = false;
    }

    /**
     * The main method for the thread.
     * Receives incoming messages from the client and executes the appropriate command.
     */
    @Override
    public void run() {
        try (
            PrintWriter serverOut = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader serverIn = 
                new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            ) {
            String input;

            // Read incoming client messages
            while((input = serverIn.readLine()) != null) {
                String[] tokens = input.split(" ", 2);
                Commands command = tokens.length > 0 ? Commands.fromString(tokens[0]) : Commands.UNKNOWN;
                String data = tokens.length > 1 ? tokens[1] : null;
                
                // Execute client commands 
                switch (command) {
                    case CONNECT:
                        if (tokens.length < 1) {
                            this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
                        }
                        this.handleConnect(serverOut, data);
                        break;
                    case DELETE:
                        this.handleDelete(serverOut, data);
                        break;
                    case DISCONNECT:
                        this.handleDisconnect(serverOut, true);
                        break;
                    case GET:
                        this.handleGet(serverOut, data);
                        break;
                    case PUT:
                        // TODO how do we diff data for key and value?? 
                        this.handlePut(serverOut, data, serverIn.readLine());
                        break;
                    default:
                        // Invalid client command
                        logger.log(Level.WARNING, "Unknown client command");
                        this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
                        break;
                }
            }
        } catch (SocketException error) {
            logger.log(Level.WARNING, "Client {0} disconnected", this.clientID);
        } catch (IOException error) {
            logger.log(Level.WARNING, error.getMessage());
        } finally {
            // Ensure client data is remove if unexpected disconnected
            this.serverClientSessionManager.disconnectClient(clientID);
        }
    }

    /**
     * Handles the CONNECT command from the client.
     * @param serverOut The PrintWriter stream to send messages to the client.
     * @param data The client ID associated with the connection.
     * @throws IOException If there is an I/O error while sending or receiving data from the client.
     */
    public void handleConnect(PrintWriter serverOut, String data) throws IOException {
        // Check if client is already connected.
        if (!this.serverClientSessionManager.addClient(data)) {
            this.sendMessageToClient(serverOut, Commands.CONNECT.toString(), ServerConstants.SERVER_ERROR);
            logger.log(Level.INFO, "Client: {0} is already connected. Disconnecting.", data);
            this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
        }
        this.clientID = data;
        this.clientConnected = true;
        this.sendMessageToClient(serverOut, Commands.CONNECT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);

        logger.log(Level.INFO, "Client: {0} has connected", data);
    }

    /**
     * 
     * @param serverOut The PrintWriter stream to send messages to the client.
     * @param key 
     * @throws IOException
     */
    public void handleDelete(PrintWriter serverOut, String key) throws IOException {
        // Client must connect first
        if (!clientConnected) {
            this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
        }

        //
        if (this.serverClientSessionManager.deleteClientData(this.clientID, key)) {
            this.sendMessageToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);
        } else {
            this.sendMessageToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_ERROR);
        }  
    }

    /**
     * Disconnects the client, updates the server's client session manager, and closes the socket connection.
     * @param serverOut PrintWriter object used to send messages to the server
     * @param clientInitiatedDisconnect Indicates whether the client initiated the disconnection or not.
     * @throws IOException if there is an I/O error while closing the socket connection
     */
    public void handleDisconnect(PrintWriter serverOut, boolean clientInitiatedDisconnect) throws IOException {
        // Remove client data
        this.serverClientSessionManager.disconnectClient(clientID);
        this.clientConnected = false;
        
        // Send disconnect message to client
        if (clientInitiatedDisconnect) {
            this.sendMessageToClient(
                serverOut, 
                Commands.DISCONNECT.toString(), 
                ServerConstants.SERVER_EXECUTED_COMMAND_OK);
        }

        // Shutdown socket
        if (!this.socket.isClosed()) {
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        }
    }

    /**
    * Handles a GET command from the client, which retrieves data for the connected client 
    * with the specified key from the server.
    * @param serverOut The PrintWriter used to send messages to the client.
    * @param key The key used to identify the data to be retrieved.
    * @throws IOException If an I/O error occurs while communicating with the client or server.
    */
    public void handleGet(PrintWriter serverOut, String key) throws IOException {
        // Client must connect first
        if (!clientConnected) {
            this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
        }
        
        // Fetch data for client
        String clientData = this.serverClientSessionManager.getClientData(this.clientID, key);
        
        // Check data is present
        if (clientData == null) {
            this.sendMessageToClient(serverOut, Commands.GET.toString(), ServerConstants.SERVER_ERROR);
        } else {
            this.sendMessageToClient(serverOut, null, clientData);
        }
    }

    /**
     * Handles a PUT command from the client, which adds or updates a key-value pair in the server's memory.
     * @param serverOut The PrintWriter used to send messages to the client.
     * @param key The key to be added or updated.
     * @param value The value to be added or updated.
     * @throws IOException if there is an error sending messages to the client or closing the socket.
     */
    public void handlePut(PrintWriter serverOut, String key, String value) throws IOException {
        // Client must connect first
        if (!clientConnected) {
            this.handleDisconnect(serverOut, ServerConstants.SERVER_DISCONNECT_ERROR);
        }

        // Server must have enough memory to store data
        if (!Server.isMemoryEnoughAvailable(key.getBytes().length + value.getBytes().length)) {
            this.sendMessageToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_ERROR);
        }
       
        //
        if (this.serverClientSessionManager.putClientData(this.clientID, key, value)) {
            this.sendMessageToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);
        } else {
            this.sendMessageToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_ERROR);
        }      
    }
    
    /**
     * Sends a message to the client.
     * @param serverOut The PrintWriter stream to send the message to.
     * @param messages The message(s) to be sent to the client.
     */
    public void sendMessageToClient(PrintWriter serverOut, String ... messages) {
        String message = String.join(" ", messages);
        serverOut.printf("%s%s", message, ServerConstants.MESSAGE_TERMINATION);
    }
}
