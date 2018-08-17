package av.is.matchmaking.api;

import java.util.Collection;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface IMatchmaking {
    
    /**
     *
     * Return collection of matchmaking managers
     *
     * @return Allocated matchmaking managers
     */
    Collection<MatchmakingManager> getManagers();
    
    /**
     *
     * Return specific matchmaking manager by matchmaking id
     *
     * @param matchmakingId String which can identify matchmaking manager
     * @return Matchmaking manager
     */
    MatchmakingManager getManager(String matchmakingId);
    
}
