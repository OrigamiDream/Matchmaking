package av.is.matchmaking.processor.command;

public class ThrowableInvalidCommand implements ThrowableCommand {
    @Override
    public boolean isInstance(CommandException exception) {
        return exception instanceof InvalidCommandException;
    }

    @Override
    public String message(CommandException exception) {
        return exception.getMessage();
    }
}
