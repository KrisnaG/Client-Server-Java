/**
 * @author Krisna Gusti
 */

package a2.src.test.utility;

import a2.src.main.utility.Validation;

public class ValidationTest {
    /**
     * Main to run tests.
     */
    public static void main(String[] args) {
        testValidateInputs();
        testNumberOfInputsValid();
        testIsInputAValidNumber();
    }

    /**
     * Test valid inputs.
     */
    private static void testValidateInputs() {
        String[] input = {"5", "8", "12"};
        int numberOfInputs = 3;
        int invalidNumberOfInputs = 1;
        boolean pass = true;

        // Test should be valid
        if (!Validation.validateInputs(input, numberOfInputs)) {
            pass = false;
        }

        // Test should be invalid
        if (Validation.validateInputs(input, invalidNumberOfInputs)) {
            pass = false;
        }

        if (pass) {
            System.out.println("Validation.validateInputs test passed.");
        } else {
            System.out.println("Validation.validateInputs test failed.");
        }
    }

    /**
     * Test valid number of inputs
     */
    private static void testNumberOfInputsValid() {
        String[] arguments = {"5", "8"};
        int numberOfInputs = 2;
        int invalidNumberOfInputs = 1;
        boolean pass = true;

        // Test should be valid
        if (!Validation.numberOfInputsValid(arguments, numberOfInputs)) {
            pass = false;
        }
        
        // Test should be invalid
        if (Validation.numberOfInputsValid(arguments, invalidNumberOfInputs)) {
            pass = false;
        }

        if (pass) {
            System.out.println("Validation.numberOfInputsValid test passed.");
        } else {
            System.out.println("Validation.numberOfInputsValid test failed.");
        }
    }

    /**
     * Test input is a valid number.
     */
    private static void testIsInputAValidNumber() {
        String input = "5";
        String invalidInput = "Five";
        boolean pass = true;

        // Test should be valid
        if (!Validation.isInputAValidNumber(input)) {
            pass = false;
        }

        // Test should be invalid
        if (Validation.isInputAValidNumber(invalidInput)) {
            pass = false;
        }

        if (pass) {
            System.out.println("Validation.isInputAValidNumber test passed.");
        } else {
            System.out.println("Validation.isInputAValidNumber test failed.");
        }
    }
}
