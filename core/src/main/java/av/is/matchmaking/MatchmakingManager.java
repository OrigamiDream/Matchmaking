package av.is.matchmaking;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface MatchmakingManager {
    
    /**
     * Publish command to redis server.
     *
     * @param command   A storage of the command.
     * @param <C>       This implements {@link Command} or both {@link Command} and {@link CommandResponse}.
     */
    <C extends Command> void publishRedis(C command);
    
    /**
     * Listen command from redis server.
     *
     * This run {@link CommandResponse#respond(Command)} to do something manually.
     *
     * @param key       The key of the command.
     * @param message   Json-serialized string to be compiled.
     */
    void handleRedis(String key, String message);
    
    /**
     *
     * Register command which implements both {@link Command} and {@link CommandResponse}.
     *
     * @param key       The key of the command.
     * @param command   A storage of the command.
     * @param <C>       This implements both {@link Command} and {@link CommandResponse}.
     */
    <C extends Command & CommandResponse> void registerRedis(String key, C command);
    
    /**
     *
     * Register command class and callback which implement both {@link Command} and {@link CommandResponse}.
     *
     * @param key       The key of the command.
     * @param command   A storage of the command.
     * @param response  A callback for the command.
     * @param <C>       This implements {@link Command}.
     * @param <R>       This implements {@link CommandResponse}.
     */
    <C extends Command, R extends CommandResponse> void registerRedis(String key, Class<C> command, R response);
    
    /**
     *
     * Register a new connection(master, slave) to be used randomly when handle/publish.
     *
     * @param connection
     * @param <R> This implements {@link SimpleRedisConnection} or annoymous {@link RedisConnection}
     */
    <R extends RedisConnection> void registerConnection(R connection);
    
}