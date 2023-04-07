/**
 * @author Krisna Gusti
 */
package a2.src.server;

/**
 * This class contains constants used in the server application.
 */
public class ServerConstants {
    // Server error codes
    public static final int EXIT_FAILURE_CONNECTION = 1;

    // Server threads
    public static final int MAX_THREAD_COUNT = 10;

    // Server reserved memory in bytes
    public static final long SERVER_RESERVED_BYTES = 8192;

    // Server inputs
    public static final int NUMBER_OF_SERVER_START_INPUTS = 1;

    // Server messages
    public static final char MESSAGE_TERMINATION = '\n';
    public static final String SERVER_USAGE = "Usage: ./startServer <port number>";
    public static final String SERVER_EXECUTED_COMMAND_OK = "OK";
    public static final String SERVER_ERROR = "ERROR";
}
