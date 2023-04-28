package test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class SimpleServer {
    public static void main(String[] args) {
        // Attempts to open port
        try (
            ServerSocket serverSocket = new ServerSocket(4444);

        ) {
            Socket clientSocket = serverSocket.accept();
            try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                Scanner scanner = new Scanner(System.in);
            ) {
                
                while (true) {
                    System.out.println(in.readLine());
                    System.out.print("Enter a response to send back: ");
                    String input = scanner.nextLine();
                    out.printf("%s%n", input);
                }
            }
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }
}
