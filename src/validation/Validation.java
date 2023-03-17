/**
 * 
 */
package a2.src.validation;

/**
 * 
 */
public class Validation {
    public static final int EXIT_FAILURE = -1;
    
    /**
     * 
     * @param input
     */
    public static void validateInputs(final String[] input, final int numberOfInputs) {
        if (!Validation.numberOfInputsValid(input, numberOfInputs)) {
            System.exit(Validation.EXIT_FAILURE);
        }

        if (!Validation.isInputAValidNumber(input[input.length - 1])) {
            System.exit(Validation.EXIT_FAILURE);
        }
    }

    /**
     * 
     * @param arguments
     * @return
     */
    public static boolean numberOfInputsValid(final String[] arguments, final int numberOfInputs) {
        if (arguments.length != numberOfInputs) {
            System.out.println("Invalid number of inputs");
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
            System.out.println("Invalid input - Not a valid number");
            return false;
        }
    }

    /**
     * 
     * @param bytes
     * @return
     */
    public static boolean isMemoryEnoughAvailable(long bytes) {
        return Runtime.getRuntime().freeMemory() + bytes 
                < Runtime.getRuntime().maxMemory();
    }
}
