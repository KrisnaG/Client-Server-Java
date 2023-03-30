/*
 * 
 */
package a2.src.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import a2.src.utility.Commands;

/**
 * 
 */
public class ServerClientHandler extends Thread {
    private Socket socket;
    private final ServerClientSessionManager serverClientSessionManager;
    private boolean clientConnected;
    private String clientID;

    /**
     * 
     * @param socket
     * @param serverClientSessionManager
     */
    public ServerClientHandler(Socket socket, ServerClientSessionManager serverClientSessionManager) {
        this.socket = socket;
        this.serverClientSessionManager = serverClientSessionManager;
        this.clientID = null;
        this.clientConnected = false;
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
                String[] tokens = input.split(" ", 2);
                Commands command = tokens.length > 0 ? Commands.fromString(tokens[0]) : null;
                String data = tokens.length > 1 ? tokens[1] : null;
                
                // Execute client commands 
                switch (command) {
                    case CONNECT:
                        if (tokens.length < 1) {
                            handleDisconnect(serverOut);
                        }
                        handleConnect(serverOut,data);
                        break;
                    case DELETE:
                        handleDelete(serverOut,data);
                        break;
                    case DISCONNECT:
                        handleDisconnect(serverOut);
                        break;
                    case GET:
                        handleGet(serverOut, data);
                        break;
                    case PUT:
                        handlePut(serverOut, data, serverIn.readLine());
                        break;
                    default:
                        // Invalid client command
                        System.err.println("Unknown client command");
                        handleDisconnect(serverOut);
                        break;
                }
            }

        } catch (IOException error) {
            // TODO
            System.err.println("IOEXCEPTION");
        } finally {
            // Ensure client data is remove if unexpected disconnected
            this.serverClientSessionManager.disconnectClient(clientID);
            try {
                this.socket.close();
            } catch (IOException error) {
                // TODO
            }
        }
        return;
    }

    /**
     * 
     * @param data
     */
    public void handleConnect(PrintWriter serverOut, String data) {
        // Check if client is already connected.
        if (!this.serverClientSessionManager.addClient(data)) {
            sendToClient(serverOut, Commands.CONNECT.toString(), ServerConstants.SERVER_ERROR);
            handleDisconnect(serverOut);
        }
        this.clientID = data;
        this.clientConnected = true;
        sendToClient(serverOut, Commands.CONNECT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);
    }

    /**
     * 
     * @param data
     */
    public void handleDelete(PrintWriter serverOut, String data) {

    }

    /**
     * 
     */
    public void handleDisconnect(PrintWriter serverOut) {
        try {
            clientConnected = false;
            serverClientSessionManager.disconnectClient(clientID);
            sendToClient(serverOut, Commands.DISCONNECT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);
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
    public void handleGet(PrintWriter serverOut, String data) {
        // Client must connect first
        if (!clientConnected) {
            handleDisconnect(serverOut);
        }
        
        String clientData = this.serverClientSessionManager.getClientData(this.clientID, data);
        
        if (clientData == null) {
            sendToClient(serverOut, Commands.GET.toString(), ServerConstants.SERVER_ERROR);
        } else {
            sendToClient(serverOut, null, clientData);
        }
    }

    /**
     * 
     * @param key
     */
    public void handlePut(PrintWriter serverOut, String key, String value) {
        // Client must connect first
        if (!clientConnected) {
            handleDisconnect(serverOut);
        }

        // Server must have enough memory to store data
        if (!Server.isMemoryEnoughAvailable(key.getBytes().length + value.getBytes().length)) {
            sendToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_ERROR);
        }

        //
        this.serverClientSessionManager.putClientData(this.clientID, key, value);
        
        //
        sendToClient(serverOut, Commands.PUT.toString(), ServerConstants.SERVER_EXECUTED_COMMAND_OK);
    }

    /**
     * 
     * @param serverOut
     * @param command
     * @param message
     */
    public void sendToClient(PrintWriter serverOut, String command, String message) {
        if (command == null) {
            serverOut.printf("%s%s", message, ServerConstants.MESSAGE_TERMINATION);
        } else {
            serverOut.printf("%s: %s%s", command, message, ServerConstants.MESSAGE_TERMINATION);
        }
    }
}
