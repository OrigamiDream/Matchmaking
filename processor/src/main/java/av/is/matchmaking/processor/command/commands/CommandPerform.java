package av.is.matchmaking.processor.command.commands;

import av.is.matchmaking.processor.MatchRegistry;
import av.is.matchmaking.processor.ServerInfo;
import av.is.matchmaking.processor.command.Command;
import av.is.matchmaking.processor.command.CommandContext;
import av.is.matchmaking.processor.command.CommandException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CommandPerform implements Command {

    private final MatchRegistry registry;

    public CommandPerform(MatchRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String[] aliases() {
        return new String[] { "perform" };
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        if(context.argsLength() < 2) {
            throw new CommandException("Command usage: perform <server-id> <command>");
        }

        String matchId = context.getString(0);
        String command = context.getJoinedString(1);
    
        List<ServerInfo> servers = new ArrayList<>();
        if(matchId.equalsIgnoreCase("-a")) {
            servers.addAll(registry.getServers());
        } else {
            Optional<ServerInfo> serverInfo = registry.getServerById(matchId);
            if(!serverInfo.isPresent()) {
                throw new CommandException("No server found named '" + matchId + "'");
            }
            servers.add(serverInfo.get());
        }
        
        servers.forEach(server -> server.performCommand(command));
    }
}
