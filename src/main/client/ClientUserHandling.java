/**
 * @author Krisna Gusti
 */
package a2.src.main.client;

import a2.src.main.utility.Commands;
import a2.src.main.utility.Validation;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that handles user input and communication with the server.
 */
public class ClientUserHandling {
    private BufferedReader clientIn;
    private PrintWriter clientOut;
    private Scanner scanner;
    private boolean disconnectFromServer;
    private static Logger logger = Logger.getLogger(ClientUserHandling.class.getName());
    
    /**
     * Constructor.
     * @param clientIn the input stream of the client's socket.
     * @param clientOut the output stream of the client's socket.
     * @param scanner the scanner to read input from the console.
     */
    public ClientUserHandling(BufferedReader clientIn, PrintWriter clientOut, Scanner scanner) {
        this.clientIn = clientIn;
        this.clientOut = clientOut;
        this.scanner = scanner;
        disconnectFromServer = false;
    }

    
    /**
     * Handles the connection of the client to the server. It prompts the user to input 
     * their username, connects to the server with the username, waits for a response 
     * from the server, and then validates the response.
     * @param clientIn the input stream of the client's socket.
     * @param clientOut the output stream of the client's socket.
     * @param scanner the scanner to read input from the console.
     * @throws IOException if there is an error with the input/output stream.
     * @throws ClientException if there is an error with the server response.
     */
    public void handleConnect() throws IOException, ClientException {
        // Get user input
        System.out.print(ClientConstants.CLIENT_USER_CONNECT_MESSAGE);
        String userInput = this.getUserInput();

        // Connect to server with username
        this.clientOut.printf("%s %s%s", ClientConstants.CLIENT_CONNECT, userInput, ClientConstants.MESSAGE_TERMINATION);
        
        // Wait for server response
        String response = this.clientIn.readLine();
        String[] responseSplit = response.split(" ", 2);
        
        // Check if server response has two parts
        if (!Validation.numberOfInputsValid(responseSplit, 2)) {
            throw new ClientException("Invalid server response");
        }

        // Error response received from server
        if (responseSplit[1].equals(ClientConstants.CLIENT_ERROR)) {
            throw new ClientException("Server connection error");
        }
        
        // Check server response is valid
        if (Commands.fromString(responseSplit[0]) != Commands.CONNECT && !responseSplit[1].equals(ClientConstants.CLIENT_OK)) {
            throw new ClientException("Invalid server response");
        }

        // Display server response
        System.out.println(response);
    }

    /**
     * Handles user requests by prompting the user for input, parsing the input,
     * and calling the appropriate handler method.
     * @throws IOException if there is an error with the input/output stream.
     * @throws ClientException if there is an error with the server response.
     * @return true if the user wishes to disconnect, otherwise, false.
     */
    public boolean handleRequest() throws IOException, ClientException {        
        // Get user command
        System.out.print(ClientConstants.CLIENT_USER_COMMAND_MESSAGE);
        String userInput = this.getUserInput();

        // Executes user command
        switch (userInput) {
            case "1":
                this.handleGet();
                break;
            case "2":
                this.handlePut();
                break;
            case "3":
                this.handleDelete();
                break;
            case "4":
                this.handleDisconnect();
                break;
            default:
                logger.log(Level.WARNING, "Unknown user command");
                break;
        }
        
        return this.disconnectFromServer;
    }

    /**
     * Handles the GET command by getting the key from the user, sending a GET 
     * request with the key to the server, waiting for the server response, and 
     * printing it to the console.
     * @throws IOException if there is an error with the input/output stream.
     * @throws ClientException if there is an error with the client response.
     */
    public void handleGet() throws IOException, ClientException {
        // Get key from user
        System.out.println(ClientConstants.CLIENT_USER_GET_MESSAGE);
        String key = this.getUserInput();

        // Check for valid key
        if (key == null || key.equals("")) {
            throw new ClientException(ClientConstants.INVALID_KEY_ERROR);
        }

        // Send GET request with key to server
        this.sendMessageToServer(Commands.GET.toString(), key);

        // Wait for server response
        String response = this.clientIn.readLine();
        System.out.println(response);
    }

    /**
     * Handles the DELETE command by getting a key from the user, sending a DELETE
     * request with the key to the server, waiting for the server response, and 
     * printing the response to the console.
     * @throws IOException if there is an error with the input/output stream.
     * @throws ClientException if there is an error with the client response.
     */
    public void handleDelete() throws IOException, ClientException {
        // Get key from user
        System.out.println(ClientConstants.CLIENT_USER_DELETE_MESSAGE);
        String key = this.getUserInput();

        // Check for valid key
        if (key == null || key.equals("")) {
            throw new ClientException(ClientConstants.INVALID_KEY_ERROR);
        }

        // Send DELETE request with key to server
        this.sendMessageToServer(Commands.DELETE.toString(), key);

        // Wait for server response
        String response = this.clientIn.readLine();
        System.out.println(response);
    }

    /**
     * Sends a DISCONNECT command to the server and waits for its response.
     * @throws IOException if there is an error with the input/output stream.
     */
    public void handleDisconnect() throws IOException {
        // Send DELETE request to server
        this.sendMessageToServer(Commands.DISCONNECT.toString());

        // Wait for server response
        String response = this.clientIn.readLine();
        System.out.println(response);

        this.disconnectFromServer = true;
    }

    /**
     * Handles the PUT command by getting key and value from the user, sending a
     * PUT request with the key and value to the server, and waiting for the server
     * response.
     * @throws IOException if there is an error with the input/output stream.
     * @throws ClientException if there is an error with the client response.
     */
    public void handlePut() throws IOException, ClientException {
        // Get key from user
        System.out.println(ClientConstants.CLIENT_USER_PUT_KEY_MESSAGE);
        String key = this.getUserInput();

        // Check for valid key
        if (key == null || key.equals("")) {
            throw new ClientException(ClientConstants.INVALID_KEY_ERROR);
        }

        // Get key from user
        System.out.println(ClientConstants.CLIENT_USER_PUT_VALUE_MESSAGE);
        String value = this.getUserInput();

        // Check for valid value
        if (value == null || value.equals("")) {
            throw new ClientException(ClientConstants.INVALID_VALUE_ERROR);
        }

        // Send PUT request with key to server
        this.sendMessageToServer(Commands.PUT.toString(), key, 
            String.valueOf(ClientConstants.MESSAGE_TERMINATION), value);

        // Wait for server response
        String response = this.clientIn.readLine();
        System.out.println(response);
    }

    /**
     * Sends a message to the server.
     * @param messages The message(s) to be sent to the server.
     */
    public void sendMessageToServer(String ... messages) {
        String message = String.join(" ", messages);
        this.clientOut.printf("%s%s", message, ClientConstants.MESSAGE_TERMINATION);
    }

    /**
     * Reads a line of input from the user and removes any trailing newline characters.
     * @return The user input string.
     */
    public String getUserInput() {
        // Get user input
        String userInput = this.scanner.nextLine();
        
        // Remove any newline characters from end
        userInput = userInput.endsWith("\n") ? userInput.substring(0, userInput.length() - 1) : userInput;

        return userInput;
    }
}
