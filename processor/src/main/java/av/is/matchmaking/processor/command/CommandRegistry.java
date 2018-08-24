package av.is.matchmaking.processor.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, Command> commandMap = new HashMap<>();
    private final List<ThrowableCommand> throwableCommands = new ArrayList<>();
    private final ThrowableDefaultCommand defaultCommand;

    public CommandRegistry() {
        this.throwableCommands.add(new ThrowableCommandFormat());
        this.throwableCommands.add(new ThrowableInvalidCommand());

        this.defaultCommand = new ThrowableDefaultCommand();
    }

    public void register(Command command) {
        for(String alias : command.aliases()) {
            if(commandMap.containsKey(alias)) {
                System.out.println("Command '" + alias + "' is overridden by '" + command.getClass().getSimpleName() + "'");
            }
            commandMap.put(alias, command);
        }
    }

    public Command getCommand(String label) {
        return commandMap.get(label);
    }

    public void dispatch(Command command, String[] arguments) throws CommandException {
        if(command == null) {
            throw new InvalidCommandException();
        }

        command.execute(new CommandContext(arguments));
    }

    public void propagateThrowable(CommandException exception) {
        ThrowableCommand throwableCommand = null;
        for(ThrowableCommand command : throwableCommands) {
            if(command.isInstance(exception)) {
                throwableCommand = command;
                break;
            }
        }
        if(throwableCommand == null) {
            throwableCommand = defaultCommand;
        }
        System.out.println(throwableCommand.message(exception));
    }

}
