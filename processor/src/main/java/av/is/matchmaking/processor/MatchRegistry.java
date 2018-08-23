package av.is.matchmaking.processor;

import av.is.matchmaking.api.MatchmakingManager;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchRegistry {

    private final ListMultimap<String, ServerInfo> servers = ArrayListMultimap.create();
    private final MatchmakingManager matchmakingManager;
    
    public MatchRegistry(MatchmakingManager matchmakingManager) {
        this.matchmakingManager = matchmakingManager;
    }
    
    public MatchmakingManager getMatchmakingManager() {
        return matchmakingManager;
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
