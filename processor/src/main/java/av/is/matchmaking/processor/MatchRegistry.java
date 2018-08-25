package av.is.matchmaking.processor;

import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.processor.command.CommandRegistry;
import av.is.matchmaking.processor.command.commands.CommandHelp;
import av.is.matchmaking.processor.command.commands.CommandPerform;
import av.is.matchmaking.processor.command.commands.CommandServers;
import av.is.matchmaking.processor.command.commands.CommandView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public final class MatchRegistry {

    private final ListMultimap<String, ServerInfo> servers = Multimaps.synchronizedListMultimap(ArrayListMultimap.create());
    private final MatchmakingManager matchmakingManager;
    private final CommandRegistry commandRegistry;
    private final ServerPool pool;
    
    private AtomicReference<ServerInfo> currentView = new AtomicReference<>(null);
    
    public MatchRegistry(MatchmakingManager matchmakingManager) {
        this.matchmakingManager = matchmakingManager;
        this.commandRegistry = new CommandRegistry();
        this.commandRegistry.register(new CommandHelp());
        this.commandRegistry.register(new CommandPerform(this));
        this.commandRegistry.register(new CommandServers(this));
        this.commandRegistry.register(new CommandView(this));

        this.pool = new ServerPool();
        Thread thread = new Thread(this.pool);
        thread.setName("Matchmaking pool");
        thread.setDaemon(true);
        thread.start();
    }
    
    public boolean isViewable(String serverName) {
        ServerInfo serverInfo = currentView.get();
        if(serverInfo == null) {
            return true;
        }
        return serverName.equalsIgnoreCase(serverInfo.getServerId());
    }
    
    public AtomicReference<ServerInfo> currentView() {
        return currentView;
    }
    
    public void setCurrentView(ServerInfo serverInfo) {
        this.currentView.set(serverInfo);
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
        return servers.entries();
    }

    public Optional<ServerInfo> getServerByMatchId(String matchId) {
        return servers.values().stream().filter(serverInfo -> serverInfo.getUniqueId() != null && serverInfo.getUniqueId().equals(matchId)).findFirst();
    }

    public Optional<ServerInfo> getServerById(String serverId) {
        return servers.values().stream().filter(serverInfo -> serverInfo.getServerId() != null && serverInfo.getServerId().equals(serverId)).findFirst();
    }

    public Collection<ServerInfo> getServers() {
        return servers.values();
    }

    public List<ServerInfo> getServers(String matchType) {
        return servers.get(matchType);
    }
    
    public List<ServerInfo> getUsableServers() {
        return getServers().stream().filter(ServerInfo::isNotUsing).collect(Collectors.toList());
    }

    public List<ServerInfo> getUsableServers(String matchType) {
        return getServers(matchType).stream().filter(ServerInfo::isNotUsing).collect(Collectors.toList());
    }

    public void addServer(String matchType, File directory, String runCommand) {
        servers.put(matchType, new ServerInfo(this, directory, runCommand));
    }

    public void addServer(String matchType, ServerInfo serverInfo) {
        servers.put(matchType, serverInfo);
    }
}
