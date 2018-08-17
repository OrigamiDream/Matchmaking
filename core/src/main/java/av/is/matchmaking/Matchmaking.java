package av.is.matchmaking;

import av.is.matchmaking.api.IMatchmaking;
import av.is.matchmaking.api.MatchmakingManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public final class Matchmaking implements IMatchmaking {
    
    private final Map<String, MatchmakingManager> managers = new HashMap<>();
    
    public static Matchmaking getInstance() {
        return Lazy.LAZY;
    }
    
    @Override
    public Collection<MatchmakingManager> getManagers() {
        return managers.values();
    }
    
    @Override
    public MatchmakingManager getManager(String matchmakingId) {
        return managers.computeIfAbsent(matchmakingId, MatchmakingManagerImpl::new);
    }
    
    static final class Lazy {
        static final Matchmaking LAZY = new Matchmaking();
    }
    
}
