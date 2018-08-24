package av.is.matchmaking.processor.command;

public class ThrowableCommandFormat implements ThrowableCommand {
    @Override
    public boolean isInstance(CommandException exception) {
        return exception instanceof CommandFormatException;
    }

    @Override
    public String message(CommandException exception) {
        return exception.getMessage();
    }
}
