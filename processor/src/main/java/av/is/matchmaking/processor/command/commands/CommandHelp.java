package av.is.matchmaking.processor.command.commands;

import av.is.matchmaking.processor.command.Command;
import av.is.matchmaking.processor.command.CommandContext;
import av.is.matchmaking.processor.command.CommandException;

public class CommandHelp implements Command {

    @Override
    public String[] aliases() {
        return new String[] { "help", "?" };
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        System.out.println("Show commands of processors:");
        System.out.println("- perform <server-id> <command>\t\tPerform command remotely to specific server. ");
        System.out.println("- servers\t\t\t\t\t\t\tShow list of allocated servers.");
    }
}
