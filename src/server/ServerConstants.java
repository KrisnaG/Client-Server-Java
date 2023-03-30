/**
 * 
 */
package a2.src.server;

/**
 * 
 */
public class ServerConstants {
    //
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE_VALIDATION = 1;
    public static final int EXIT_FAILURE_CONNECTION = 2;
    public static final int EXIT_FAILURE_SERVER = 3;
    public static final int EXIT_FAILURE_INVALID_CLIENT_COMMAND = 4;

    //
    public static final int MAX_THREAD_COUNT = 10;

    //
    public static final long SERVER_RESERVED_BYTES = 8192;

    //
    public static final int NUMBER_OF_SERVER_START_INPUTS = 1;
    public static final int NUMBER_OF_CLIENT_START_INPUTS = 2;

    //
    public static final char MESSAGE_TERMINATION = '\n';

    //
    public static final String SERVER_USAGE = "Usage: ./startServer <port number>";
    public static final String SERVER_EXECUTED_COMMAND_OK = "OK";
    public static final String SERVER_ERROR = "ERROR";
}
