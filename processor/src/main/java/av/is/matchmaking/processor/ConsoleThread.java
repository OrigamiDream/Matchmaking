package av.is.matchmaking.processor;

import av.is.matchmaking.processor.command.CommandException;
import av.is.matchmaking.processor.command.CommandRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class ConsoleThread implements Runnable {

    private final MatchRegistry registry;

    ConsoleThread(MatchRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("> ");
            String command = scanner.nextLine();
            String[] arguments;
            if (command.contains(" ")) {
                arguments = command.split(" ");
            } else {
                arguments = new String[] { command };
            }
            String label = arguments[0];
            List<String> args = new ArrayList<>();
            if(arguments.length > 1) {
                for(int i = 1; i < arguments.length; i++) {
                    String argument = arguments[i];
                    if(argument != null && argument.length() > 0) {
                        args.add(argument);
                    }
                }
            }
            System.out.println("Console performed command: " + command);
            CommandRegistry commandRegistry = registry.getCommandRegistry();

            try {
                commandRegistry.dispatch(commandRegistry.getCommand(label), args.toArray(new String[0]));
            } catch (CommandException e) {
                commandRegistry.propagateThrowable(e);
            }
        }
    }
}
