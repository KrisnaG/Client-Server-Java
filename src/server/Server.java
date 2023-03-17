/**
 * 
 */
package a2.src.server;

import a2.src.validation.Validation;

/**
 * 
 */
public class Server {
    private static final int NUMBER_OF_SERVER_INPUTS = 1;

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        Validation.validateInputs(args, NUMBER_OF_SERVER_INPUTS);
    }
}
