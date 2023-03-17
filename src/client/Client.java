/*
 * 
 */
package a2.src.client;

import a2.src.validation.Validation;

/**
 * 
 */
public class Client {
    private static final int NUMBER_OF_CLIENT_INPUTS = 2;

    /**
     * Entry point.
     * @param args
     */
    public static void main(String[] args) {
        Validation.validateInputs(args, NUMBER_OF_CLIENT_INPUTS);
    }
}
