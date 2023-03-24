/**
 * 
 */
package a2.src.utility;

/**
 * 
 */
public class Validation {
    /**
     * 
     * @param input
     */
    public static void validateInputs(final String[] input, final int numberOfInputs) {
        if (!Validation.numberOfInputsValid(input, numberOfInputs)) {
            System.exit(Constants.EXIT_FAILURE_VALIDATION);
        }

        if (!Validation.isInputAValidNumber(input[input.length - 1])) {
            System.exit(Constants.EXIT_FAILURE_VALIDATION);
        }
    }

    /**
     * 
     * @param arguments
     * @return
     */
    public static boolean numberOfInputsValid(final String[] arguments, final int numberOfInputs) {
        if (arguments.length != numberOfInputs) {
            System.err.println("Invalid number of inputs");
            return false;
        }
        return true;
    }

    /**
     * 
     * @param input
     * @return
     */
    public static boolean isInputAValidNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException error) {
            System.err.println("Invalid input - Not a valid number");
            return false;
        }
    }
}
