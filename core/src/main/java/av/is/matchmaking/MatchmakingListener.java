package av.is.matchmaking;

import redis.clients.jedis.JedisPubSub;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public final class MatchmakingListener extends JedisPubSub {
    
    private final MatchmakingManager manager;
    
    public MatchmakingListener(MatchmakingManager manager) {
        this.manager = manager;
    }
    
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        try {
            String commandType = channel.split(":")[1];
            manager.handleRedis(commandType, message);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
