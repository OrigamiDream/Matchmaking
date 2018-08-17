package av.is.matchmaking;

import av.is.matchmaking.api.MatchmakingManager;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
final class MatchmakingListener extends JedisPubSub {
    
    private final MatchmakingManager manager;
    
    public MatchmakingListener(MatchmakingManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        try {
            manager.handleRedis(channel.split(":")[1], message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
