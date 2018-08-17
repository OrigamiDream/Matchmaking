package av.is.matchmaking;

import av.is.matchmaking.api.Command;
import av.is.matchmaking.api.CommandResponse;
import av.is.matchmaking.api.CommandStorage;
import av.is.matchmaking.api.MatchmakingManager;
import av.is.matchmaking.api.MergedCommandStorage;
import av.is.matchmaking.api.RedisConnection;
import av.is.matchmaking.api.SimpleCommandStorage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static av.is.matchmaking.RedisType.MASTER;
import static av.is.matchmaking.RedisType.SLAVE;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
final class MatchmakingManagerImpl implements MatchmakingManager {
    
    public static final String REDIS_CHANNEL = "matchmaking.commands";
    
    private static final Gson GSON = new Gson();
    private static final Random RANDOM = new Random();
    
    private final String matchmakingId;
    
    private final MatchmakingListener listener;
    private final Map<String, CommandStorage> commandStorages = new HashMap<>();
    private final Multimap<RedisType, RedisConnection> connections = ArrayListMultimap.create();
    
    private final JedisPool masterPool;
    private final JedisPool slavePool;
    
    private final Map<String, JedisPool> pools = new ConcurrentHashMap<>();
    
    public MatchmakingManagerImpl(String matchmakingId) {
        this.matchmakingId = matchmakingId;
        this.listener = new MatchmakingListener(this);
        
        this.masterPool = createJedis(getMasterConnection());
        this.slavePool = createJedis(getSlaveConnection());
        
        new Thread("Matchmaking Redis") {
            @Override
            public void run() {
                try(Jedis jedis = slavePool.getResource()) {
                    jedis.psubscribe(listener, REDIS_CHANNEL + ":*");
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    @Override
    public String getMatchmakingId() {
        return matchmakingId;
    }
    
    @Override
    public <C extends Command> void publishRedis(C command) {
        String key = null;
        for(Map.Entry<String, CommandStorage> entry : commandStorages.entrySet()) {
            if(entry.getValue().getCommand() == command.getClass()) {
                key = entry.getKey();
            }
        }
        
        if(key == null) {
            throw new IllegalArgumentException("Unknown key for '" + command.getClass().getSimpleName() + "'");
        }
        String finalKey = key;
        new Thread(() -> {
            try(Jedis jedis = masterPool.getResource()) {
                jedis.publish(REDIS_CHANNEL + ":" + finalKey, GSON.toJson(command));
            } catch(Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    @Override
    public void handleRedis(String key, String message) {
        if(commandStorages.containsKey(key)) {
            CommandStorage storage = commandStorages.get(key);
            Command command = GSON.fromJson(message, storage.getCommand());
            if(command.validate(matchmakingId) && storage.getCommandResponse() != null) {
                storage.getCommandResponse().respond(command);
            }
        }
    }
    
    @Override
    public <C extends Command & CommandResponse> void registerRedis(String key, Class<C> command) {
        commandStorages.put(key, new MergedCommandStorage() {
            @Override
            public Class<C> mergedCommand() {
                return command;
            }
        });
    }
    
    @Override
    public <C extends Command, R extends CommandResponse> void registerRedis(String key, Class<C> command, R response) {
        commandStorages.put(key, new SimpleCommandStorage() {
            @Override
            public Class<C> getCommand() {
                return command;
            }
    
            @Override
            public R getCommandResponse() {
                return response;
            }
        });
    }
    
    @Override
    public <R extends RedisConnection> void registerConnection(R connection) {
        connections.put(connection.getType(), connection);
    }
    
    public RedisConnection getMasterConnection() {
        return getConnection(true, null);
    }
    
    public RedisConnection getSlaveConnection() {
        return getConnection(false, null);
    }
    
    public RedisConnection getConnection(boolean writeable, String name) {
        List<RedisConnection> connections = getConnections(writeable, name);
        if(connections.size() > 0) {
            return connections.get(RANDOM.nextInt(connections.size()));
        }
        return writeable ? DEFAULT_WRITE_CONNECTION : DEFAULT_READ_CONNECTION;
    }
    
    public List<RedisConnection> getConnections(boolean writeable, String name) {
        return connections.get(writeable ? MASTER : SLAVE)
                .stream()
                .filter(connection -> name == null || connection.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }
    
    public JedisPool createJedis(RedisConnection connection) {
        return pools.computeIfAbsent(connection.getHost() + "/" + connection.getPort(), __ -> {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxWaitMillis(1000L);
            jedisPoolConfig.setMinIdle(5);
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPoolConfig.setMaxTotal(20);
            jedisPoolConfig.setBlockWhenExhausted(true);
            
            return new JedisPool(jedisPoolConfig, connection.getHost(), connection.getPort());
        });
    }
}
