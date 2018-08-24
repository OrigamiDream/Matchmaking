package av.is.matchmaking.processor;

import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.processor.command.CommandRegistry;
import av.is.matchmaking.processor.command.commands.CommandHelp;
import av.is.matchmaking.processor.command.commands.CommandPerform;
import av.is.matchmaking.processor.command.commands.CommandServers;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public final class MatchRegistry {

    private final ListMultimap<String, ServerInfo> servers = ArrayListMultimap.create();
    private final MatchmakingManager matchmakingManager;
    private final CommandRegistry commandRegistry;
    private final ServerPool pool;
    
    public MatchRegistry(MatchmakingManager matchmakingManager) {
        this.matchmakingManager = matchmakingManager;
        this.commandRegistry = new CommandRegistry();
        this.commandRegistry.register(new CommandHelp());
        this.commandRegistry.register(new CommandPerform(this));
        this.commandRegistry.register(new CommandServers(this));

        this.pool = new ServerPool();
        Thread thread = new Thread(this.pool);
        thread.setName("Matchmaking pool");
        thread.setDaemon(true);
        thread.start();
    }

    public ServerPool getPool() {
        return pool;
    }

    public MatchmakingManager getMatchmakingManager() {
        return matchmakingManager;
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }

    public Collection<Map.Entry<String, ServerInfo>> getMappedServers() {
        synchronized (this) {
            return servers.entries();
        }
    }

    public Optional<ServerInfo> getServerByMatchId(String matchId) {
        synchronized (this) {
            return servers.values().stream().filter(serverInfo -> serverInfo.getUniqueId() != null && serverInfo.getUniqueId().equals(matchId)).findFirst();
        }
    }

    public Optional<ServerInfo> getServerById(String serverId) {
        synchronized (this) {
            return servers.values().stream().filter(serverInfo -> serverInfo.getServerId() != null && serverInfo.getServerId().equals(serverId)).findFirst();
        }
    }

    public Collection<ServerInfo> getServers() {
        synchronized (this) {
            return servers.values();
        }
    }

    public List<ServerInfo> getServers(String matchType) {
        synchronized(this) {
            return servers.get(matchType);
        }
    }

    public List<ServerInfo> getUsableServers(String matchType) {
        return getServers(matchType).stream().filter(ServerInfo::isNotUsing).collect(Collectors.toList());
    }

    public void addServer(String matchType, File directory, String runCommand) {
        synchronized(this) {
            servers.put(matchType, new ServerInfo(directory, runCommand));
        }
    }

    public void addServer(String matchType, ServerInfo serverInfo) {
        synchronized(this) {
            servers.put(matchType, serverInfo);
        }
    }
}
