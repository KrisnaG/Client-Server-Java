/**
 * @author Krisna Gusti
 */
package a2.src.utility;

/**
 * 
 */
public enum Commands {
    CONNECT("CONNECT"),
    PUT("PUT"),
    GET("GET"),
    DELETE("DELETE"),
    DISCONNECT("DISCONNECT");

    private final String commandString;

    /**
     * 
     * @param commandString
     */
    Commands(String commandString) {
        this.commandString = commandString;
    }

    /**
     * 
     * @return
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Converts the command in String to enum.
     * @param commandString Command to convert.
     * @return Command if valid, otherwise, null.
     */
    public static Commands fromString(String commandString) {
        for (Commands command : Commands.values()) {
            if (command.getCommandString().equals(commandString)) {
                return command;
            }
        }
        return null;
    }
}
