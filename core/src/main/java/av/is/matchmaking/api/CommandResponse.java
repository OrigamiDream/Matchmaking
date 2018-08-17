package av.is.matchmaking.api;

/**
 * Created by OrigamiDream on 2018-08-16.
 */
public interface CommandResponse {
    
    /**
     *
     * Responds specific Redis command.
     *
     * You can respond various commands with only CommandResponse.
     *
     * @param command
     */
    void respond(Command command);
}