/**
 * 
 */
package a2.src.utility;

/**
 * 
 */
public enum Command {
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
    Command(String commandString) {
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
     * 
     * @param commandString
     * @return
     */
    public static Command fromString(String commandString) {
        for (Command command : Command.values()) {
            if (command.getCommandString().equals(commandString)) {
                return command;
            }
        }
        return null;
    }
}
