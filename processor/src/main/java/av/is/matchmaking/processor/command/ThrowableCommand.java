package av.is.matchmaking.processor.command;

public interface ThrowableCommand {

    boolean isInstance(CommandException exception);

    String message(CommandException exception);

}
