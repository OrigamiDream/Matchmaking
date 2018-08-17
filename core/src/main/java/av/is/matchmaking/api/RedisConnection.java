package av.is.matchmaking.api;

import av.is.matchmaking.RedisType;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface RedisConnection {
    
    /**
     *
     * Master or Slave
     *
     * Master is writing, slave is reading.
     *
     * @return MASTER/SLAVE
     */
    RedisType getType();
    
    /**
     *
     * Identifier of the connection.
     *
     * @return name
     */
    String getName();
    
    /**
     *
     * Redis address of the connection.
     *
     * @return address
     */
    String getHost();
    
    /**
     *
     * Redis port of the connection.
     *
     * @return port
     */
    int getPort();
}
