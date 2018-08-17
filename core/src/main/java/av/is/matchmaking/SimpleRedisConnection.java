package av.is.matchmaking;

import av.is.matchmaking.api.RedisConnection;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public final class SimpleRedisConnection implements RedisConnection {
    
    private final RedisType redisType;
    private final String name;
    private final String host;
    private final int port;
    
    public SimpleRedisConnection(RedisType redisType, String name, String host, int port) {
        this.redisType = redisType;
        this.name = name;
        this.host = host;
        this.port = port;
    }
    
    @Override
    public RedisType getType() {
        return redisType;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getHost() {
        return host;
    }
    
    @Override
    public int getPort() {
        return port;
    }
}
