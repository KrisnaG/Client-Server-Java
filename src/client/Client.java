/*
 * 
 */
package a2.src.client;

import a2.src.utility.Commands;
import a2.src.utility.Validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 
 */
public class Client {
    private String hostName;
    private int portNumber;

    /**
     * 
     * @param hostName
     * @param portNumber
     */
    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * 
     */
    public void connectToHost() {
        // Attempts to connect to host on given port
        try (
            Socket socket = new Socket(this.hostName, this.portNumber);
            PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clientIn = 
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in);
            ) {
            // connect to server with username
            handleConnect(clientIn, clientOut, scanner);

            // Execute user commands
            while (true) {
                handleRequest(clientIn, clientOut, scanner);
            }
        } catch (UnknownHostException error) {
            System.err.println(error.getMessage());
        } catch (IOException error) {
            System.err.println(error.getMessage());
        } catch (Exception error) {
            // TODO
        }
    }

    /**
     * 
     * @param clientIn
     * @param clientOut
     * @param scanner
     * @throws Exception
     */
    public void handleConnect(BufferedReader clientIn, PrintWriter clientOut, Scanner scanner) throws Exception {
        // Get user input
        System.out.print(ClientConstants.CLIENT_USER_CONNECT_MESSAGE);
        String userInput = scanner.nextLine();
        
        // Remove any newline characters
        userInput.replaceAll("\n", "");

        // Connect to server with username
        clientOut.printf("%s %s%s", ClientConstants.CLIENT_CONNECT, userInput, ClientConstants.MESSAGE_TERMINATION);
        
        // TODO
        String[] response = clientIn.readLine().split(" ", 2);
        System.out.println(response);

        if (response.length != 2) {
            // TODO make new exception class and add message
            throw new Exception("");
        }
        // Server response valid
        if (Commands.fromString(response[0]) == null && response[1] != ClientConstants.CLIENT_OK) {
            // TODO make new exception class
            throw new Exception("Invalid server message");
        }
    }

    /**
     * 
     * @param clientIn
     * @param clientOut
     * @param scanner
     * @throws Exception
     */
    public void handleRequest(BufferedReader clientIn, PrintWriter clientOut, Scanner scanner) throws Exception {
        // print user commands
        System.out.print(ClientConstants.CLIENT_USER_COMMAND_MESSAGE);
        String userInput = scanner.nextLine();
    }

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        // Validate inputs
        if (!Validation.validateInputs(args, ClientConstants.NUMBER_OF_CLIENT_START_INPUTS)) {
            System.out.println(ClientConstants.CLIENT_USAGE);
            System.exit(Validation.EXIT_FAILURE_VALIDATION);
        }
       
        // Inputs should be validated
        Client client = new Client(args[0], Integer.parseInt(args[1]));

        // Connect to host
        client.connectToHost();
    }
}
