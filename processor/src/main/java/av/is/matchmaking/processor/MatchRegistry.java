package av.is.matchmaking.processor;

import av.is.matchmaking.api.MatchmakingManager;

/**
 * Created by OrigamiDream on 2018-08-17.
 */
public class MatchRegistry {
    
    private final MatchmakingManager matchmakingManager;
    
    public MatchRegistry(MatchmakingManager matchmakingManager) {
        this.matchmakingManager = matchmakingManager;
    }
    
    public MatchmakingManager getMatchmakingManager() {
        return matchmakingManager;
    }
}
