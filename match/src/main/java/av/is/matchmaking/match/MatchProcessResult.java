package av.is.matchmaking.match;

import av.is.matchmaking.api.Command;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchProcessResult extends Command {
    
    public static final String MATCH_PROCESS_RESPONSE_KEY = "match-process-response";
    
    private final String matchId;
    private final String serverId;
    private final String address;
    private final int port;
    
    private final int result;
    
    public MatchProcessResult(String matchId, String serverId, String address, int port, ProcessResult result) {
        this.matchId = matchId;
        this.serverId = serverId;
        this.address = address;
        this.port = port;
        this.result = result.ordinal();
    }
    
    public String getMatchId() {
        return matchId;
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
    
    public ProcessResult getResult() {
        return ProcessResult.values()[result];
    }
}
