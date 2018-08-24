package av.is.matchmaking.processor.command.commands;

import av.is.matchmaking.processor.MatchRegistry;
import av.is.matchmaking.processor.ServerInfo;
import av.is.matchmaking.processor.command.Command;
import av.is.matchmaking.processor.command.CommandContext;
import av.is.matchmaking.processor.command.CommandException;

import java.util.Map;

public class CommandServers implements Command {

    private final MatchRegistry registry;

    public CommandServers(MatchRegistry registry) {
        this.registry = registry;
    }

    @Override
    public String[] aliases() {
        return new String[] { "servers" };
    }

    @Override
    public void execute(CommandContext context) throws CommandException {
        for(Map.Entry<String, ServerInfo> entry : registry.getMappedServers()) {
            ServerInfo serverInfo = entry.getValue();

            StringBuilder message = new StringBuilder();
            message.append(entry.getKey()).append(" - ");
            if(serverInfo.isUsing()) {
                if(serverInfo.getServerId() != null) {
                    message.append(serverInfo.getUniqueId()).append(" - ONLINE");
                } else {
                    message.append("LOADING");
                }
            } else {
                message.append("OFFLINE");
            }
            System.out.println(message);
        }
    }
}
