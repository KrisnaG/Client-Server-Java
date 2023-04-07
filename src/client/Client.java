/**
 * @author Krisna gusti
 */
package a2.src.client;

import a2.src.utility.Validation;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * The Client class represents a client that connects to a server.
 */
public class Client {
    private String hostName;
    private int portNumber;

    /**
     * Constructs a new Client with the given host name and port number.
     * @param hostName the host name of the server to connect to.
     * @param portNumber the port number to use for the connection.
     */
    public Client(String hostName, int portNumber) {
        this.hostName = hostName;
        this.portNumber = portNumber;
    }

    /**
     * Attempts to connect to the specified host and port, and executes user commands with the server.
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
            boolean disconnect = false;

            // For user handling
            ClientUserHandling clientUserHandling = new ClientUserHandling(clientIn, clientOut, scanner);

            // Connect to server with username
            clientUserHandling.handleConnect();

            // Execute user commands with Server
            while (!disconnect) {
                disconnect = clientUserHandling.handleRequest();
            }
        } catch (UnknownHostException error) {
            System.err.println(error.getMessage());
        } catch (IOException error) {
            System.err.println(error.getMessage());
        } catch (ClientException error) {
            System.err.println(error.getMessage());
        }
    }

    /**
     * Entry point for the client application.
     * @param args an array of input arguments. Should contain the host name and port number.
     */
    public static void main(String[] args) {
        // Validate inputs
        if (!Validation.validateInputs(args, ClientConstants.NUMBER_OF_CLIENT_START_INPUTS)) {
            System.out.println(ClientConstants.CLIENT_USAGE);
            System.exit(Validation.EXIT_FAILURE_VALIDATION);
        }
       
        // Create client - Inputs should be  already validated
        Client client = new Client(args[0], Integer.parseInt(args[1]));

        // Connect to host
        client.connectToHost();
    }
}
