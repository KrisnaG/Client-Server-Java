/**
 * @author Krisna Gusti
 */
package a2.src.test.utility;

import a2.src.main.utility.Commands;

public class CommandsTest {
    /**
     * Main to run tests.
     */
    public static void main(String[] args) {
        testFromString();
    }

    /**
     * Test commands from string
     */
    public static void testFromString() {
        boolean pass = true;

        // Test a valid command string
        String validCommandString = "PUT";
        Commands validCommand = Commands.fromString(validCommandString);
        if (validCommand != Commands.PUT) {
            pass = false;
            System.out.println("fromString with valid command string. Test failed");
        }

        // Test an invalid command string
        String invalidCommandString = "INVALID";
        Commands invalidCommand = Commands.fromString(invalidCommandString);
        if (invalidCommand != null) {
            pass = false;
            System.out.println("fromString with invalid command string. Test failed");
        }

        if (pass) {
            System.out.println("Commands test passed.");
        }
    }    
}
