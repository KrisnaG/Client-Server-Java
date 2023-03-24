/*
 * 
 */
package a2.src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import a2.src.utility.Command;

/**
 * 
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private final ClientIdManager clientIdManager;
    private boolean clientConnected;
    private String clientID;
    private Map<String, String> clientData;

    /**
     * 
     * @param socket
     * @param clientData
     */
    public ClientHandler(Socket socket, ClientIdManager clientIdManager) {
        this.socket = socket;
        this.clientIdManager = clientIdManager;
        this.clientConnected = false;
        this.clientID = null;
        this.clientData = new HashMap<>();
    }

    /**
     * 
     */
    public void run() {
        try (
            PrintWriter serverOut = new PrintWriter(this.socket.getOutputStream(), true);
            BufferedReader serverIn = 
                new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            ) {
            
            String input;

            // Read incoming client messages
            while((input = serverIn.readLine()) != null) {
                String[] inputLine = input.split(" ", 2);
                Command command = inputLine.length > 0 ? Command.fromString(inputLine[0]) : null;
                String data = inputLine.length > 1 ? inputLine[1] : null;
                
                // Invalid client command
                if (command == null) {
                    System.err.println("Unknown client command");
                    handleDisconnect();
                }

                // Execute client commands 
                switch (command) {
                    case CONNECT:
                        if (inputLine.length < 1) {
                            handleDisconnect();
                        }
                        handleConnect(data);
                        break;
                    case DELETE:
                        handleDelete(data);
                        break;
                    case DISCONNECT:
                        handleDisconnect();
                        break;
                    case GET:
                        handleGet(data);
                        break;
                    case PUT:
                        handlePut(data);
                        break;
                    default:
                        handleDisconnect();
                        break;
                }
            }

        } catch (IOException error) {
            System.err.println("");
        } finally {
            try {
                this.socket.close();
            } catch (IOException error) {

            }
        }
        
        return;
    }

    /**
     * 
     * @param data
     */
    public void handleConnect(String data) {
        if (!clientIdManager.addClient(data)) {
            handleDisconnect();
        } 
        clientID = data;
        // Check if client is connected.
    }

    /**
     * 
     * @param data
     */
    public void handleDelete(String data) {

    }

    /**
     * 
     */
    public void handleDisconnect() {
        try {
            clientIdManager.disconnectClient(clientID);
            clientData.clear();
            this.socket.shutdownInput();
            this.socket.shutdownOutput();
            this.socket.close();
        } catch (IOException error) {
            System.err.println("Server - Error shutting down client socket");
        }
        return;
    }

    /**
     * 
     * @param data
     */
    public void handleGet(String data) {
        if (!clientConnected) {
            handleDisconnect();
        }
    }

    /**
     * 
     * @param data
     */
    public void handlePut(String data) {
        if (!clientConnected || Server.isMemoryEnoughAvailable(data.getBytes().length)) {
            handleDisconnect();
        }
    }
}
