package av.is.matchmaking.match;

import av.is.matchmaking.api.Command;

public class MatchProcessLoader extends Command {
    
    public static final String MATCH_PROCESS_LOADER_KEY = "match-process-loader";
    
    private final String uniqueId;
    private final String matchType;
    private final String serverId;
    private final String address;
    private final int port;
    private final MatchSwitch matchSwitch;
    
    public MatchProcessLoader(String uniqueId, String matchType, String serverId, String address, int port, MatchSwitch matchSwitch) {
        this.uniqueId = uniqueId;
        this.matchType = matchType;
        this.serverId = serverId;
        this.address = address;
        this.port = port;
        this.matchSwitch = matchSwitch;
    }
    
    public String getUniqueId() {
        return uniqueId;
    }
    
    public String getMatchType() {
        return matchType;
    }
    
    public String getServerId() {
        return serverId;
    }
    
    public String getAddress() {
        return address;
    }
    
    public int getPort() {
        return port;
    }
    
    public MatchSwitch getMatchSwitch() {
        return matchSwitch;
    }
}
