package av.is.matchmaking.processor.command;

public interface Command {

    String[] aliases();

    void execute(CommandContext context) throws CommandException;

}
