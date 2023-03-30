/**
 * 
 */
package a2.src.client;

/**
 * 
 */
public class ClientConstants {
    // Client inputs
    public static final int NUMBER_OF_CLIENT_START_INPUTS = 2;

    // Client out message
    public static final char MESSAGE_TERMINATION = '\n';
    public static final String CLIENT_CONNECT = "CONNECT";
    public static final String CLIENT_OK = "OK";

    // User messages
    public static final String CLIENT_USAGE = "Usage: ./startClient <hostname> <port number>";
    public static final String CLIENT_USER_CONNECT_MESSAGE = "Enter username: ";
    public static final String CLIENT_USER_COMMAND_MESSAGE = "Available options:\n"
                                                           + "1. GET\n"
                                                           + "2. PUT\n"
                                                           + "3. DELETE\n"
                                                           + "4. DISCONNECT\n";
}
