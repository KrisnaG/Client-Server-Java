package test.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class simpleClient {

    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 4444);
            Scanner scanner = new Scanner(System.in);
            PrintWriter clientOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clientIn = 
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {

            while (true) {
                System.out.print("Enter something to send: ");
                String input = scanner.nextLine();
                
                System.out.println("newline? y/n");
                if (scanner.nextLine().equals("n")) {
                    clientOut.printf("%s", input);
                } else {
                    clientOut.printf("%s%n", input);
                }
                System.out.println("\n");

                System.out.println("Wait for server response? y/n");
                if (scanner.nextLine().equals("y")) {
                    System.out.println("RETURN: \n");
                    System.out.println(clientIn.readLine());
                }
                System.out.println("\n");
            }

        } catch (IOException error) {
            System.err.println(error.getMessage());
        }
    }
    
}
