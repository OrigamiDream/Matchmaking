package av.is.matchmaking.processor.command.commands;

import av.is.matchmaking.processor.MatchRegistry;
import av.is.matchmaking.processor.ServerInfo;
import av.is.matchmaking.processor.command.Command;
import av.is.matchmaking.processor.command.CommandContext;
import av.is.matchmaking.processor.command.CommandException;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CommandView implements Command {
    
    private final MatchRegistry registry;
    
    public CommandView(MatchRegistry registry) {
        this.registry = registry;
    }
    
    @Override
    public String[] aliases() {
        return new String[] { "view" };
    }
    
    @Override
    public void execute(CommandContext context) throws CommandException {
        if(context.argsLength() == 0) {
            AtomicReference<ServerInfo> currentView = registry.currentView();
            if(currentView.get() != null) {
                System.out.println("You are watching '" + currentView.get().getServerId() + "'");
            } else {
                throw new CommandException("There is no watching server currently.");
            }
        } else {
            String targetServer = context.getJoinedString(0);
            if(targetServer.equalsIgnoreCase("-a")) {
                registry.setCurrentView(null);
                System.out.println("Now you can see all console.");
            } else {
                Optional<ServerInfo> serverInfo = registry.getServerById(targetServer);
                if(!serverInfo.isPresent()) {
                    throw new CommandException("Server '" + targetServer + "' is not found.");
                }
                registry.setCurrentView(serverInfo.get());
                System.out.println("Set current view server as '" + targetServer + "'.");
            }
        }
    }
}