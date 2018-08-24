package av.is.matchmaking.processor.command;

public class ThrowableDefaultCommand implements ThrowableCommand {
    @Override
    public boolean isInstance(CommandException exception) {
        return true;
    }

    @Override
    public String message(CommandException exception) {
        return exception.getMessage();
    }
}
