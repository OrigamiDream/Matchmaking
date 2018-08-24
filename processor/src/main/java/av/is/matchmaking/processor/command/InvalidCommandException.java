package av.is.matchmaking.processor.command;

public class InvalidCommandException extends CommandException {

    public InvalidCommandException() {
        super("Unknown command. Type \"help\" to get information.");
    }
}
