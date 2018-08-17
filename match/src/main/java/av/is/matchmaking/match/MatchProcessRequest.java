package av.is.matchmaking.match;

import av.is.matchmaking.api.Command;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchProcessRequest extends Command {
    
    public static final String MATCH_PROCESS_REQUEST_KEY = "match-process-request";
    
    private final String senderId;
    private final String matchType;
    
    public MatchProcessRequest(String senderId, String matchType) {
        this.senderId = senderId;
        this.matchType = matchType;
    }
    
    public String getSenderId() {
        return senderId;
    }
    
    public String getMatchType() {
        return matchType;
    }
}
