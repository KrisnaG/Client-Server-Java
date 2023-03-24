/*
 * 
 */
package a2.src.client;

import a2.src.utility.Validation;
import a2.src.utility.Command;
import a2.src.utility.Constants;

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
    private final String hostName;
    private final int portNumber;

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
            BufferedReader clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            ) {
                
            // Connect to server with username
            System.out.print(Constants.CLIENT_USER_CONNECT_MESSAGE);
            String input = scanner.nextLine();
            input.replaceAll("\n", "");
            clientOut.printf("%s %s%s", Command.CONNECT.getCommandString(), input, Constants.MESSAGE_TERMINATION);
            
            // Execute user commands
            while (true) {
                System.out.print(Constants.CLIENT_USER_COMMAND_MESSAGE);
                
            }

        } catch (UnknownHostException error) {
            System.err.println("Unknown hostname");
        } catch (IOException error) {
            System.err.println("Unable to connect");
        }
    }

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        // Validate inputs
        Validation.validateInputs(args, Constants.NUMBER_OF_CLIENT_START_INPUTS);
       
        // Inputs should be validated
        Client client = new Client(args[0], Integer.parseInt(args[1]));

        // Connect to host
        client.connectToHost();
    }
}
